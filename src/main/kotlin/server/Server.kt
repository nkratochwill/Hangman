package server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

fun main(args: Array<String>) {
    val server = Server(args[0].toInt())
    server.run()
}

val clientList = mutableListOf<Server.ClientHandler>()
var tries = 5
//Wörter als arrays oder listen umsetzen um besser zu struktierien und auflösung von wöret einfacher zu amchen
val word = "wasef"
var concealedword = word.conceal()

class Server(private val port: Int) {
    fun run() {
        val scope = CoroutineScope(Job())
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

    class ClientHandler(var client: Socket) {
        private val scope = CoroutineScope(Job())
        private val reader = Scanner(client.getInputStream())
        private val writer = client.getOutputStream()
        private var running = true


        init {
            scope.launch { run() }
        }

        private fun run() {
            write("HALLo wiklokemmn zu Hangman")
            write("You have $tries tries and the ${String(concealedword)} has ${concealedword.size} letters")


            gameloop@ while (running) {
                val message = reader.nextLine()
                //TODO else umschreiben, da wenn client mittem im spiel dc schickt er nur leerzeichen und das sorgt wegen while schleife für dauer output von else
                when {
                    tries == 0 -> {
                        writeToAll("You lose. The word was $word")
                        //word = newWord()
                        tries = 5
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
                        //word = newWord()
                        tries = 5
                    }
                    else -> writeToAll("${String(concealedword)} and ${--tries} tries left")

                }
            }
        }


        private fun write(message: String) {
            writer.write("$message \n".toByteArray(Charset.defaultCharset()))
            writer.flush()
        }

        private fun writeToAll(message: String) {
            for (client in clientList) {
                client.writer.write("$message \n".toByteArray(Charset.defaultCharset()))
                client.writer.flush()
            }
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

        private fun newWord(): CharArray {
            TODO("Implement file access and random word selection from file")
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

private fun String.conceal(): CharArray {
    val copyOfSolution = this.toCharArray()
    for (i in this.indices) {
        copyOfSolution[i] = '.'
    }
    return copyOfSolution
}