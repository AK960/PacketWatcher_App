package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

suspend fun startUdpClient(
    ipAddress: String,
    portNumber: Int,
    nPackets: Int,
    udpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    withContext(Dispatchers.IO) launch@{
        try {
            myLog(msg = "[UDP-Client] Sending $nPackets packets to $ipAddress:$portNumber")

            // Set variables and create socket
            var prevTime = System.currentTimeMillis()
            val destAddress = InetAddress.getByName(ipAddress)
            val socket = DatagramSocket()

            for (i in 1..nPackets) {
                // IAT handling
                val currTime = System.currentTimeMillis()
                val deltaTime = currTime - prevTime
                prevTime = currTime

                // Build message
                val packetMessage = "[P#$i][DT:$deltaTime ms] $udpMessage"

                // Create and send packet
                val udpPacket = DatagramPacket(packetMessage.toByteArray(), packetMessage.length, destAddress, portNumber)
                socket.send(udpPacket)
                withContext(Dispatchers.Main) { printOnUi("[UDP-Client]", packetMessage) }

                // delay 1s
                delay(1000)
            }

            // Receive response
            val buffer = ByteArray(1024)
            val responsePacket = DatagramPacket(buffer, buffer.size)
            socket.receive(responsePacket)
            val udpResponse = String(responsePacket.data, 0, responsePacket.length)

            // Log response to UI
            val senderAddress = responsePacket.address.hostAddress
            withContext(Dispatchers.Main) {
                printOnUi("[UDP-Server][$senderAddress] ", udpResponse)
            }

            // Close socket
            socket.close()
        } catch (e: Exception) {
            myLog(type = "error", msg = "[UDP-Client] Error: ${e.message}")
            withContext(Dispatchers.Main) { printOnUi("[UDP-Client] ", "Error: ${e.message}") }
            e.printStackTrace()
        }
    }
}