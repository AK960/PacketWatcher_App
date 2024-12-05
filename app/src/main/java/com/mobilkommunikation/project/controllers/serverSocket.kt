package com.mobilkommunikation.project.controllers

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

fun getRandomAvailablePort(): Int {
    ServerSocket(0).use { socket ->
        return socket.localPort
    }
}

fun handleStartServerRequest(
    ipAddress: String,
    portNumber: Int = getRandomAvailablePort(),
    protocolSelected: String
) {
    when (protocolSelected) {
        "TCP" -> startTcpServer(ipAddress, portNumber)
        "UDP" -> startUdpServer(ipAddress, portNumber)
    }
}

fun startTcpServer (
    ipAddress: String,
    portNumber: Int = getRandomAvailablePort()
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

fun startUdpServer(
    ipAddress: String,
    portNumber: Int = getRandomAvailablePort()
) {
    val socket = DatagramSocket(portNumber)
    myLog(msg = "serverSocket: UDP-Server is listening on port $portNumber")

    val buffer = ByteArray(1024)
    while (true) {
        try {
            val packet = DatagramPacket(buffer, buffer.size)
            socket.receive(packet)
            val received = String(packet.data, 0, packet.length)
            myLog(msg = "serverSocket: Received message: $received from ${packet.address}:${packet.port}")

            // TODO: Implement Logic here to display stream in outputFields and to respond to messages from client
            // TODO: Implement Exception handling
            thread {
                val response = "Message received!"
                val responsePacket =
                    DatagramPacket(response.toByteArray(), response.length, packet.address, packet.port)
                socket.send(responsePacket)
                myLog(msg = "serverSocket: Sent response: $response to ${packet.address}:${packet.port}")
            }
        } catch (e: Exception) {
            myLog(type = "error", msg = "serverSocket: Error: ${e.message}")
            e.printStackTrace()
        }
    }
}

