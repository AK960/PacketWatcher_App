package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val serverAddress = serverSocket.inetAddress.hostAddress
        myLog(type = "debug", msg = "tcpServer: Server started on port $portNumber in thread ${Thread.currentThread().name}.")

        launch {
            while (isActive) {
                delay(10000L)
                myLog(type = "debug", msg = "tcpServer: Server listening on port $portNumber in thread ${Thread.currentThread().name}.")
            }
        }

        try {
            while (true) {
                val clientSocket: Socket = serverSocket.accept()
                val clientAddress = clientSocket.inetAddress.hostAddress
                myLog(msg = "tcpServer: Client $clientAddress connected. Trying to read data ...")

                launch {
                    try {
                        // Read Message
                        val clientReader = clientSocket.getInputStream().bufferedReader()
                        val clientMessage = clientReader.readLine()
                        myLog(msg = "tcpServer: Received message from client $clientAddress: $clientMessage")

                        // Process Message
                        val serverMessage = "TCP-Server $serverAddress: Message received and processed."

                        // Return to UI
                        withContext(Dispatchers.Main) {
                            printOnUi("TCP-Client: $clientAddress", "Message: $clientMessage")
                        }

                        // Respond to Client
                        val serverWriter = clientSocket.getOutputStream().bufferedWriter()
                        serverWriter.write(serverMessage)
                        serverWriter.newLine()
                        serverWriter.flush()
                        myLog(msg = "tcpServer: Sent response to client $clientAddress")
                    } catch (e: Exception) {
                        myLog(type = "error", msg = "tcpServer: Error processing message: ${e.message}")
                        e.printStackTrace()
                    } finally {
                        clientSocket.close()
                        myLog(msg = "tcpServer: Client Socket closed.")
                    }
                }
            }
        } catch (e: IOException) {
            myLog(type = "error", msg = "tcpServer: General error: ${e.message}")
            e.printStackTrace()
        } finally {
            serverSocket.close()
            myLog(msg = "tcpServer: ServerSocket closed.")
        }
    }
}
