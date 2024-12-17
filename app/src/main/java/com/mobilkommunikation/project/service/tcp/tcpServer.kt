package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

fun startTcpServer (
    portNumber: Int,
    scope: CoroutineScope,
    printOnUi: (message: String, String) -> Unit
): Job {
    return scope.launch(Dispatchers.IO) {
        try {
            // Create Socket
            val serverSocket = ServerSocket(portNumber)

            // Logging
            withContext(Dispatchers.Main) { printOnUi("[TCP-Server]", "Server listening on port ::$portNumber") }
            myLog(type = "debug", msg = "tcpServer: Server started on port $portNumber in thread ${Thread.currentThread().name}.")
            while (isActive) {
                delay(5000L)
                myLog(type = "debug", msg = "tcpServer: Server listening on port $portNumber in thread ${Thread.currentThread().name}.")
            }

            try {
                // Connect Client
                while (true) {
                    val clientSocket: Socket = serverSocket.accept()
                    val clientAddress = clientSocket.inetAddress.hostAddress
                    myLog(msg = "tcpServer: Client $clientAddress connected. Trying to read data ...")

                    try {
                        // Start Server
                        // Read Message
                        val clientReader = clientSocket.getInputStream().bufferedReader()
                        val clientMessage = clientReader.readLine()
                        myLog(msg = "tcpServer: Received message from client $clientAddress: $clientMessage")

                        // Return to UI
                        withContext(Dispatchers.Main) { printOnUi("[TCP-Server]:", "Received message from $clientAddress: $clientMessage") }

                        // Respond to Client
                        val serverMessage = "TCP-Server: Message acknowledged."
                        val serverWriter = clientSocket.getOutputStream().bufferedWriter()
                        serverWriter.write(serverMessage)
                        serverWriter.newLine()
                        serverWriter.flush()
                        myLog(msg = "tcpServer: Sent response to client $clientAddress")
                        withContext(Dispatchers.Main) { printOnUi("[TCP-Server]", "Acknowledgement sent to $clientAddress") }
                    } catch (e: Exception) {
                        myLog(type = "error", msg = "tcpServer: Failed to start server. Exit with error: ${e.message}")
                        e.printStackTrace()
                    } finally {
                        clientSocket.close()
                        withContext(Dispatchers.Main) { printOnUi("TCP-Server", "Client-Socket closed.") }
                        myLog(msg = "tcpServer: Client Socket closed.")
                    }
                }
            } catch (e: IOException) {
                myLog(type = "error", msg = "tcpServer: Failed to accept client connection. Exit with error: ${e.message}")
                e.printStackTrace()
            } finally {
                serverSocket.close()
                withContext(Dispatchers.Main) { printOnUi("TCP-Server", "Server-Socket closed.") }
                myLog(msg = "tcpServer: ServerSocket closed.")
            }
        } catch(e: IOException) {
            myLog(type = "error", msg = "tcpServer: Failed to create socket. Exit with error: ${e.message}")
            e.printStackTrace()
        } finally {
            withContext(Dispatchers.Main) { printOnUi("TCP-Server", "Server stopped.") }
        }
    }
}
