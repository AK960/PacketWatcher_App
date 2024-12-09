package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.getAvailablePort
import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket

fun startTcpServer (
    ipAddress: String,
    portNumber: Int = getAvailablePort()
) = runBlocking {
    launch {
        myLog(msg = "tcpServer: Preparing TCP-Server.")

        val inetAddress = InetAddress.getByName(ipAddress)
        val serverSocket = ServerSocket(portNumber, 0, inetAddress)

        try {
            myLog(msg = "tcpServer: Server listening on ${serverSocket.localSocketAddress}.")
            while (true) {
                val clientSocket = serverSocket.accept()
                myLog(msg = "tcpServer: Client connected: ${clientSocket.inetAddress.hostAddress}")

            }
        } catch (e: IOException) {
            myLog(type = "error", msg = "tcpServer: Error: ${e.message}")
            e.printStackTrace()
        }
    }

    // Create a server socket to listen on random port
}

/*
package com.mobilkommunikation.project.controllers

import com.mobilkommunikation.project.utils.getAvailablePort
import com.mobilkommunikation.project.utils.myLog
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

fun startTcpServer (
    ipAddress: String,
    portNumber: Int = getAvailablePort()
) {
    myLog(msg = "serverSocket: Preparing TCP-Server")

    // Create server socket to listen on random port
    val serverSocket = ServerSocket(portNumber)
    myLog(msg = "serverSocket: Server is listening on port $portNumber")

    while (true) {
        try {
            val clientSocket: Socket = serverSocket.accept()
            myLog(msg = "serverSocket: Client connected: ${clientSocket.inetAddress.hostAddress}")

            // TODO: Implement Logic here to display stream in outputFields and to respond to messages from client
            // TODO: Implement Exception handling
            thread {
                clientSocket.use {
                    val output = it.getOutputStream()
                    output.write("serverSocket: Connected to TCP-Server at ${serverSocket.inetAddress.hostAddress}".toByteArray())
                }
            }
            myLog(msg = "serverSocket: Closing TCP connection with client ${clientSocket.inetAddress.hostAddress}")
            clientSocket.close()
        } catch (e: Exception) {
            myLog(type = "error", msg = "serverSocket: Error: ${e.message}")
            e.printStackTrace()
        }
    }
}
*/

