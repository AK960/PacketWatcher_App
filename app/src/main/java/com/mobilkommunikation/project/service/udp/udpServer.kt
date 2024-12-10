package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.getAvailablePort
import com.mobilkommunikation.project.utils.myLog
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.concurrent.thread

fun startUdpServer(
    ipAddress: String,
    portNumber: Int = getAvailablePort()
) {
    val socket = DatagramSocket(portNumber)
    myLog(msg = "udpServer: UDP-Server is listening on port $portNumber")

    val buffer = ByteArray(1024)
    while (true) {
        try {
            val packet = DatagramPacket(buffer, buffer.size)
            socket.receive(packet)
            val received = String(packet.data, 0, packet.length)
            myLog(msg = "udpServer: Received message: $received from ${packet.address}:${packet.port}")

            // TODO: Implement Logic here to display stream in outputFields and to respond to messages from client
            // TODO: Implement Exception handling
            thread {
                val response = "Message received!"
                val responsePacket =
                    DatagramPacket(response.toByteArray(), response.length, packet.address, packet.port)
                socket.send(responsePacket)
                myLog(msg = "udpServer: Sent response: $response to ${packet.address}:${packet.port}")
            }
        } catch (e: Exception) {
            myLog(type = "error", msg = "udpServer: Error: ${e.message}")
            e.printStackTrace()
        }
    }
}
