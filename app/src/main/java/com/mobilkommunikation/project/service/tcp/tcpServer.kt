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
    onClientMessage: (ServerSocket, String, String) -> Unit
): Job {
    return scope.launch(Dispatchers.IO) {
        var serverSocket: ServerSocket? = null
        try {
            // Create Socket with reuse address option
            serverSocket = ServerSocket(portNumber).apply {
                reuseAddress = true
            }

            // Logging
            withContext(Dispatchers.Main) { onClientMessage(serverSocket, "[TCP-Server]", "Listening on ::${serverSocket.localPort}") }
            myLog(msg = "[TCP-Server] Server started: Port = $portNumber; Thread = ${Thread.currentThread().name}")

            try {
                // Continuously accept client connections
                while (true) {
                    val clientSocket: Socket = serverSocket.accept()
                    val clientAddress = clientSocket.inetAddress.hostAddress
                    myLog(msg = "[TCP-Server] $clientAddress connected. Reading message ...")

                    try {
                        // Initialize a reader for the client socket
                        val clientReader = clientSocket.getInputStream().bufferedReader()
                        val serverWriter = clientSocket.getOutputStream().bufferedWriter()

                        var nPackets = 0 // Counter for the number of messages received
                        var prevTime = System.currentTimeMillis()

                        while (true) {
                            // Read Message
                            val clientMessage = clientReader.readLine() ?: break // Break the loop if null (client disconnected)
                            myLog(msg = "[TCP-Server] Message: $clientMessage")

                            val currTime = System.currentTimeMillis()
                            val deltaTime = currTime - prevTime
                            prevTime = currTime
                            nPackets++ // Increment the message counter

                            // Return to UI
                            withContext(Dispatchers.Main) {
                                onClientMessage(serverSocket, "[TCP-Server][$clientAddress][P#$nPackets][IAT:$deltaTime ms] ", clientMessage)
                            }

                            // Respond to Client
                            val serverMessage = "Message acknowledged"
                            serverWriter.write(serverMessage)
                            serverWriter.newLine()
                            serverWriter.flush()
                            myLog(msg = "[TCP-Server] Response sent to $clientAddress")
                        }
                    } catch (e: Exception) {
                        myLog(type = "error", msg = "[TCP-Server] Error processing client messages: ${e.message}")
                        e.printStackTrace()
                    } finally {
                        clientSocket.close()
                        myLog(msg = "[TCP-Server] Client Socket closed.")
                    }
                }
            } catch (e: IOException) {
                myLog(type = "error", msg = "[TCP-Server] Failed to accept client connection. Exit with error: ${e.message}")
                e.printStackTrace()
            }
        } catch(e: IOException) {
            myLog(type = "error", msg = "[TCP-Server] Failed to create socket. Exit with error: ${e.message}")
            e.printStackTrace()
        } finally {
            serverSocket?.close()
            myLog(msg = "[TCP-Server] ServerSocket closed.")
            withContext(Dispatchers.Main) { onClientMessage(serverSocket!!, "[TCP-Server]", "Server stopped.") }
        }
    }
}