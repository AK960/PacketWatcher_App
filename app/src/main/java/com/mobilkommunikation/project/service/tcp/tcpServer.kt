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
        // Initialize ServerSocket
        var serverSocket: ServerSocket? = null

        // 1. Try: Create ServerSocket
        try {
            // Create ServerSocket with reuse address option
            serverSocket = ServerSocket(portNumber).apply {
                reuseAddress = true
            }
            // Logging
            myLog(msg = "[TCP-Server] ServerSocket created on port $portNumber in thread ${Thread.currentThread().name}.")
            withContext(Dispatchers.Main) { onClientMessage(serverSocket, "[TCP-Server]", "Listening on ::${serverSocket.localPort}") }

            // 2. Try: Accept client connections
            try {
                // Continuously accept client connections
                while (true) {
                    val clientSocket: Socket = serverSocket.accept()
                    val clientAddress = clientSocket.inetAddress.hostAddress
                    myLog(msg = "[TCP-Server] $clientAddress connected. Reading message(s) ...")
                    withContext(Dispatchers.Main) { onClientMessage(serverSocket, "[TCP-Server] ", "[$clientAddress] connected.") }

                    // 3. Try: Process client messages
                    try {
                        // Variables
                        var nPackets = 1 // Counter for the number of messages received
                        var refTime: Long? = null

                        // Initialize reader and writer
                        val clientReader = clientSocket.getInputStream().bufferedReader()
                        val serverWriter = clientSocket.getOutputStream().bufferedWriter()

                        // Continuously read client messages
                        while (true) {
                            // Set time
                            val recvTime = System.currentTimeMillis()
                            // Read Message
                            val clientMessage = clientReader.readLine() ?: break // Break the loop if null (client disconnected)
                            myLog(msg = "[TCP-Server] Message: $clientMessage")

                            if (refTime == null) {
                                refTime = recvTime
                                myLog(msg = "[TCP-Server] First packet in connection: No IAT calculated.")
                                withContext(Dispatchers.Main) {
                                    onClientMessage(serverSocket, "[TCP-Server][P#$nPackets] ", "First packet: No IAT calculated.")
                                    onClientMessage(serverSocket, "[TCP-Server][P#$nPackets] ", clientMessage)
                                }
                                nPackets ++
                            } else {
                                val iat = recvTime - refTime
                                refTime = recvTime
                                // Logging
                                myLog(msg = "[TCP-Server] IAT: $iat ms")
                                withContext(Dispatchers.Main) {
                                    onClientMessage(serverSocket, "[TCP-Server][P#$nPackets] ", "Inter-Arrival-Time (IAT): $iat ms]")
                                    onClientMessage(serverSocket, "[TCP-Server][P#$nPackets] ", clientMessage)
                                }
                                nPackets++
                            }
                        }

                        // Respond to Client
                        val serverMessage = "Messages acknowledged."
                        serverWriter.write(serverMessage)
                        serverWriter.newLine()
                        serverWriter.flush()
                        myLog(msg = "[TCP-Server] Response sent to $clientAddress")

                    } catch (e: Exception) {
                        myLog(type = "error", msg = "[TCP-Server] Error processing client messages: ${e.message}")
                        e.printStackTrace()
                    } finally {
                        clientSocket.close()
                        myLog(msg = "[TCP-Server] Client Socket closed.")
                    }
                }
            // 2. Catch: Failed to accept client connection
            } catch (e: IOException) {
                myLog(type = "error", msg = "[TCP-Server] Failed to accept client connection. Exit with error: ${e.message}")
                e.printStackTrace()
            }
        // 1. Catch: Failed to create ServerSocket
        } catch(e: IOException) {
            myLog(type = "error", msg = "[TCP-Server] Failed to create socket. Exit with error: ${e.message}")
            e.printStackTrace()
        } finally {
            serverSocket?.close()
            myLog(msg = "[TCP-Server] ServerSocket closed.")
            if (serverSocket != null) {
                withContext(Dispatchers.Main) { onClientMessage(serverSocket, "[TCP-Server]", "Server stopped.") }
            }
        }
    }
}