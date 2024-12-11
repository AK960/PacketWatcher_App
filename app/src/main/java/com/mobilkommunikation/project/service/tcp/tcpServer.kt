package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

fun startTcpServer (
    portNumber: Int
): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        val serverSocket = ServerSocket(portNumber)
        myLog(type = "debug", msg = "startTcpServer: TCP-Server is listening on port $portNumber in thread ${Thread.currentThread().name}.")

        launch {
            while (isActive) {
                delay(10000L)
                myLog(type = "debug", msg = "startTcpServer: Server is still listening on port $portNumber in thread ${Thread.currentThread().name}.")
            }
        }

        try {
            while (true) {
                val clientSocket: Socket = serverSocket.accept()
                myLog(msg = "startTcpServer: Client connected: ${clientSocket.inetAddress.hostAddress}")

                launch {
                    myLog(type = "debug", msg = "startTcpServer: Applying logic on thread ${Thread.currentThread().name}.")

                    clientSocket.use {
                        val output = it.getOutputStream()
                        output.write("startTcpServer: Connected to TCP-Server at ${serverSocket.inetAddress.hostAddress}".toByteArray())
                        output.flush()

                        myLog(msg = "startTcpServer: Sent message to client ${clientSocket.inetAddress.hostAddress}")
                    }
                }
            }

        } catch (e: IOException) {
            myLog(type = "error", msg = "startTcpServer: Error: ${e.message}")
            e.printStackTrace()
        } finally {
            serverSocket.close()
            myLog(msg = "startTcpServer: Server socket closed.")
        }
    }
}
