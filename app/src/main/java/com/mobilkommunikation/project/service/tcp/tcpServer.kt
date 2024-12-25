package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
        var serverSocket: ServerSocket? = null
        try {
            // Create Socket with reuse address option
            serverSocket = ServerSocket(portNumber).apply {
                reuseAddress = true
            }

            // Logging
            withContext(Dispatchers.Main) { printOnUi("[TCP-Server]", "Listening on ::${serverSocket.localPort}") }
            myLog(msg = "[TCP-Server] Server started: Port = $portNumber; Thread = ${Thread.currentThread().name}")

            try {
                // Continuously accept client connections
                while (true) {
                    val clientSocket: Socket = serverSocket.accept()
                    val clientAddress = clientSocket.inetAddress.hostAddress
                    myLog(msg = "[TCP-Server] $clientAddress connected. Reading message ...")

                    try {
                        // Read Message
                        val clientReader = clientSocket.getInputStream().bufferedReader()
                        val clientMessage = clientReader.readLine()
                        myLog(msg = "[TCP-Server] Message: $clientMessage")

                        // Return to UI
                        withContext(Dispatchers.Main) { printOnUi("[TCP-Server][$clientAddress]", clientMessage) }

                        // Respond to Client
                        val serverMessage = "Message acknowledged"
                        val serverWriter = clientSocket.getOutputStream().bufferedWriter()
                        serverWriter.write(serverMessage)
                        serverWriter.newLine()
                        serverWriter.flush()
                        myLog(msg = "[TCP-Server] Response sent to $clientAddress")
                    } catch (e: Exception) {
                        myLog(type = "error", msg = "[TCP-Server] Failed to start server. Exit with error: ${e.message}")
                        e.printStackTrace()
                    } finally {
                        clientSocket.close()
                        myLog(msg = "[TCP-Server] Client Socket closed.")
                    }
                }
            } catch (e: IOException) {
                myLog(type = "error", msg = "[TCP-Server] Failed to accept client connection. Exit with error: ${e.message}")
                e.printStackTrace()
            } finally {
                serverSocket.close()
                myLog(msg = "[TCP-Server] ServerSocket closed.")
            }
        } catch(e: IOException) {
            myLog(type = "error", msg = "[TCP-Server] Failed to create socket. Exit with error: ${e.message}")
            e.printStackTrace()
        } finally {
            withContext(Dispatchers.Main) { printOnUi("[TCP-Server]", "Server stopped.") }
        }
    }
}