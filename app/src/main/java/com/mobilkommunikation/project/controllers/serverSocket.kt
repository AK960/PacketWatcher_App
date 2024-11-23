package com.mobilkommunikation.project.controllers

import java.net.ServerSocket
import java.net.Socket

fun startServerSocket (
    ipAddress: String,
    portNumber: Int = getRandomAvailablePort(),
    protocolSelected: String
) {
    myLog(msg = "serverSocket: Preparing $protocolSelected-Server")

    // Create server socket to listen on random port
    val serverSocket = ServerSocket(portNumber)
    myLog(msg = "serverSocket: Server is listening on port $portNumber")

    while (true) {
        val clientSocket: Socket = serverSocket.accept()
        myLog(msg = "serverSocket: Client connected: ${clientSocket.inetAddress.hostAddress}")

        clientSocket.getOutputStream().write("Welcome to Server ${serverSocket.inetAddress.hostAddress}".toByteArray())

        myLog(msg = "serverSocket: Closing connection")
        clientSocket.close()
    }
}

fun getRandomAvailablePort(): Int {
    ServerSocket(0).use { socket ->
        return socket.localPort
    }
}
