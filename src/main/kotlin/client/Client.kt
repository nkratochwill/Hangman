package client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

fun main(args: Array<String>) {
    val client = Client(args[0], args[1].toInt())
    client.run()
}

/**
 * The client class
 * @param address the address the client will connect to
 * @param port the port that will be used to connect to the server
 */
class Client(address: String, port: Int) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val socket = Socket(address, port)
    private val reader: Scanner = Scanner(socket.getInputStream())
    private val writer = socket.getOutputStream()
    private var connected = true

    fun run() {
        scope.launch { read() }
        while (connected) {
            val message = readLine() ?: ""
            if (message == "exit") {
                write(message)
                shutdown()
                println("Shutting down")
                break
            }
            write(message)
        }
    }

    /**
     * Reads received messages
     */
    private fun read() {
        while (connected && reader.hasNextLine()) {
            println("Server: ${reader.nextLine()}")
        }
    }

    /**
     * Sends a message to the current client
     * @param message the message that will be send
     */
    private fun write(message: String) {
        writer.write("$message\n".toByteArray(Charset.defaultCharset()))
        writer.flush()
    }

    private fun shutdown() {
        reader.close()
        writer.close()
        socket.close()
        connected = false

    }
}