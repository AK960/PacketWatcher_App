package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.getAvailablePort
import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

var udpServerJob: Job? = null

fun startUdpServer(
    portNumber: Int = getAvailablePort()
): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        val socket = DatagramSocket(portNumber)
        myLog(type = "debug", msg = "startUdpServer: UDP-Server is listening on port $portNumber in thread ${Thread.currentThread().name}.")

        launch {
            while (isActive) {
                delay(10000L)
                myLog(type = "debug", msg = "startUdpServer: Server is still listening on port $portNumber in thread ${Thread.currentThread().name}.")
            }
        }

        val buffer = ByteArray(1024)
        try {
            while (isActive) {
                try {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)
                    val received = String(packet.data, 0, packet.length)
                    myLog(msg = "udpServer: Received message: $received from ${packet.address}:${packet.port}")

                    // Display the received message in the output field
                    withContext(Dispatchers.Main) {
                        updateOutputField("Received: $received from ${packet.address}:${packet.port}")
                    }

                    // Respond to the received message
                    launch {
                        val response = "Message received!"
                        val responsePacket = DatagramPacket(response.toByteArray(), response.length, packet.address, packet.port)
                        socket.send(responsePacket)
                        myLog(msg = "udpServer: Sent response: $response to ${packet.address}:${packet.port}")
                    }

                } catch (e: Exception) {
                    myLog(type = "error", msg = "udpServer: Error receiving packet: ${e.message}")
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            myLog(type = "error", msg = "udpServer: General error: ${e.message}")
            e.printStackTrace()
        } finally {
            socket.close()
            myLog(msg = "udpServer: Datagram socket closed.")
        }
    }
}

// Placeholder function to update the output field
fun updateOutputField(message: String) {
    // Implement the logic to update the output field in your UI
    myLog(msg = "updateOutputField: $message")
}