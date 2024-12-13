package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

@OptIn(DelicateCoroutinesApi::class)
fun startTcpServer (
    portNumber: Int,
    printOnUi: (message: String, String) -> Unit
): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        val serverSocket = ServerSocket(portNumber)
        myLog(type = "debug", msg = "startTcpServer: TCP-Server started on port $portNumber in thread ${Thread.currentThread().name}.")

        launch {
            while (isActive) {
                delay(10000L)
                myLog(type = "debug", msg = "startTcpServer: TCP-Server listening on port $portNumber in thread ${Thread.currentThread().name}.")
            }
        }

        try {
            while (true) {
                val clientSocket: Socket = serverSocket.accept()
                val clientAddress = clientSocket.inetAddress.hostAddress
                val serverAddress = serverSocket.inetAddress.hostAddress
                myLog(msg = "startTcpServer: Client $clientAddress connected. Trying to read data ...")

                launch {
                    try {
                        // Read Message
                        val clientReader = clientSocket.getInputStream().bufferedReader()
                        val clientMessage = clientReader.readLine()
                        myLog(msg = "startTcpServer: Received message from client $clientAddress: $clientMessage")

                        // Process Message
                        val serverMessage = "TCP-Server $serverAddress: Message received and processed."

                        // Return to UI
                        printOnUi("TCP-Client: $clientAddress", "Message: $serverMessage")
                            // TODO: Implement Logic to return message to client

                        // Respond to Client
                        val serverWriter = clientSocket.getOutputStream().bufferedWriter()
                        serverWriter.write(serverMessage)
                        serverWriter.newLine()
                        serverWriter.flush()
                    } catch (e: Exception) {
                        myLog(type = "error", msg = "startTcpServer: Error processing message: ${e.message}")
                        e.printStackTrace()
                    } finally {
                        clientSocket.close()
                        myLog(msg = "startTcpServer: Client Socket closed.")
                    }
                }
            }
        } catch (e: IOException) {
            myLog(type = "error", msg = "startTcpServer: Error starting server: ${e.message}")
            e.printStackTrace()
        } finally {
            serverSocket.close()
            myLog(msg = "startTcpServer: ServerSocket closed.")
        }
    }
}
