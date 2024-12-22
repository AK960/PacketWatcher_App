package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

suspend fun startUdpClient(
    ipAddress: String,
    portNumber: Int,
    udpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    withContext(Dispatchers.IO) {
        myLog(msg = "udpClient: Sending UPD-packet to $ipAddress:$portNumber. Sending message: $udpMessage")
        try {
            // Create socket and send message
            val socket = DatagramSocket()
            val destAddress = InetAddress.getByName(ipAddress)
            val udpPacket = DatagramPacket(udpMessage.toByteArray(), udpMessage.length, destAddress, portNumber)
            withContext(Dispatchers.Main) { printOnUi("[UDP-Client]", "Sending packet via ::${socket.localPort}") }
            socket.send(udpPacket)

            // Receive response
            val buffer = ByteArray(1024)
            val responsePacket = DatagramPacket(buffer, buffer.size)
            socket.receive(responsePacket)
            val udpResponse = String(responsePacket.data, 0, responsePacket.length)

            // Log to UI
            withContext(Dispatchers.Main) { printOnUi("[UDP-Client]", "Response: $udpResponse") }

            // Close socket
            socket.close()
        } catch (e: Exception) {
            myLog(type = "error", msg = "udpClient: Error: ${e.message}")
            withContext(Dispatchers.Main) { printOnUi("[UDP-Client]", "Error: ${e.message}") }
            e.printStackTrace()
        }
    }
    // TODO: Implement transmission logic here with flexible protocol parameter
}