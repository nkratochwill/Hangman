package server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.random.Random

fun main(args: Array<String>) {
    val server = Server(args[0].toInt())
    server.run()
}

/**
 * The server class
 * @param port The port that the server will be hosted on
 */
class Server(private val port: Int) {
    private companion object {
        val scope = CoroutineScope(Dispatchers.Default)
        val clientList = mutableListOf<ClientHandler>()
        var tries = 0
        //Wörter als arrays oder listen umsetzen um besser zu struktierien und auflösung von wöret einfacher zu amchen
        var word = newWord()
        var concealedword = word.conceal()
    }

    fun run() {
        var server: ServerSocket? = null
        try {
            server = ServerSocket(port)
            println("Server is running on port ${server.localPort}")
            while (true) {
                val client = server.accept()
                println("Client connected: ${client.inetAddress.hostAddress}")

                scope.launch { clientList.add(ClientHandler(client)) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            server?.close()
        }
    }

    /**
     * Handles client input
     * @param client The inbound client socket
     */
    class ClientHandler(var client: Socket) {
        private val reader = Scanner(client.getInputStream())
        private val writer = client.getOutputStream()
        private var running = true

        init {
            scope.launch { run() }
        }

        private fun run() {
            write("HALLo wiklokemmn zu Hangman")
            write("You have 10 tries and the ${String(concealedword)} has ${concealedword.size} letters")


            gameloop@ while (running) {
                val message = reader.nextLine()
                //else umschreiben, da wenn client mittem im spiel dc schickt er nur leerzeichen und das sorgt wegen while schleife für dauer output von else
                when {
                    tries == 10 -> {
                        writeToAll("You lose. The word was $word")
                        word = newWord()
                        concealedword = word.conceal()
                        tries = 0
                        writeToAll("You have 10 tries and the ${String(concealedword)} has ${concealedword.size} letters")
                    }
                    message == "exit" -> {
                        shutdown()
                        break@gameloop
                    }
                    message.length == 1 && message in word && '.' in concealedword -> {
                        concealedword = word.unconceal(concealedword, message[0])
                        writeToAll(String(concealedword))
                    }
                    message == word -> {
                        writeToAll("Du hast gewonnen euda, das Wort war $word")
                        word = newWord()
                        concealedword = word.conceal()
                        loadhighscorelist(tries)
                        tries = 0
                        writeToAll("You have 10 tries and the ${String(concealedword)} has ${concealedword.size} letters")
                    }

                    else -> writeToAll("${String(concealedword)} and ${++tries} tries")
                }
            }
        }

        /**
         * Sends a message to the current client
         * @param message the message that will be send
         */
        private fun write(message: String) {
            writer.write("$message \n".toByteArray(Charset.defaultCharset()))
            writer.flush()
        }

        /**
         * Sends a message to all clients
         * @param message the message that will be send
         */
        private fun writeToAll(message: String) {
            for (client in clientList) {
                client.writer.write("$message \n".toByteArray(Charset.defaultCharset()))
                client.writer.flush()
            }
        }

        /**
         * Loads the highscore list
         * @param tries if provided, updates highscore list with given tries
         */
        private fun loadhighscorelist(tries: Int = 0) {
            val highscore = mutableListOf<String>()
            val f = File("src/main/resources/highscore.txt")
            if (tries != 0) {
                f.appendText(tries.toString() + "\n")
                f.forEachLine { highscore.add(it) }
                highscore.sort()
            } else {
                f.forEachLine { highscore.add(it) }
            }
            var output = ""
            highscore.forEachIndexed { i, s -> output += "${i.inc()}. $s Versuche\n" }
            writeToAll(output)

        }

        private fun shutdown() {
            reader.close()
            writer.close()
            running = false
            println("${client.remoteSocketAddress} disconnected")
            client.close()
        }

    }
}

private fun newWord(): String {
    val wordlist: List<String>
    val f = File("src/main/resources/words.txt")
    wordlist = f.readLines()
    return wordlist[Random.nextInt(0, wordlist.size)]
}

private fun String.conceal(): CharArray {
    val copyOfSolution = this.toCharArray()
    for (i in this.indices) {
        copyOfSolution[i] = '.'
    }
    return copyOfSolution
}

private fun String.unconceal(word: CharArray, letter: Char): CharArray {
    val copyOfSolution = this.toCharArray()
    for (i in this.indices) {
        if (word[i] == '.' && this[i] != letter) {
            copyOfSolution[i] = '.'
        }
    }
    return copyOfSolution
}