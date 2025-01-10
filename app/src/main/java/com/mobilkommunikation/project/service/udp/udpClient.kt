package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

suspend fun startUdpClient(
    ipAddress: String,
    portNumber: Int,
    nPackets: Int,
    udpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    withContext(Dispatchers.IO) {
        myLog(msg = "[UDP-Client] Sending $nPackets packets to $ipAddress:$portNumber.")

        // Create Socket
        val socket = DatagramSocket()
        socket.soTimeout = 5000

        try {
            var refTime: Long? = null
            val destAddress = InetAddress.getByName(ipAddress)

            for (i in 1..nPackets) {
                // Calculate Inter Arrival Time
                val sendTime = System.currentTimeMillis()

                if (refTime == null) {
                    refTime = sendTime
                    myLog(msg = "[UDP-Client] First packet in stream: No IAT calculated.")
                    withContext(Dispatchers.Main) { printOnUi("[UDP-Client][P#$i] ", "First packet: No IAT calculated.") }
                    val packetMessage = "[P#$i] $udpMessage"
                    val udpPacket = DatagramPacket(packetMessage.toByteArray(), packetMessage.length, destAddress, portNumber)
                    socket.send(udpPacket)
                } else {
                    val iat = sendTime - refTime
                    refTime = sendTime
                    myLog(msg = "[UDP-Client] IAT: $iat ms")
                    withContext(Dispatchers.Main) { printOnUi("[UDP-Client][P#$i] ", "Inter-Arrival-Time (IAT): $iat ms") }
                    val packetMessage = "[P#$i] $udpMessage"
                    val udpPacket = DatagramPacket(packetMessage.toByteArray(), packetMessage.length, destAddress, portNumber)
                    socket.send(udpPacket)
                }
                // delay 1s
                delay(1000)
            }

            // Receive response
            try {
                val buffer = ByteArray(1024)
                val responsePacket = DatagramPacket(buffer, buffer.size)
                socket.receive(responsePacket)
                val udpResponse = String(responsePacket.data, 0, responsePacket.length)

                withContext(Dispatchers.Main) {
                    printOnUi("[UDP-Server]", udpResponse)
                }
            } catch (e: SocketTimeoutException) {
                myLog(type = "error", msg = "[UDP-Client] Error receiving response: ${e.message}")
                withContext(Dispatchers.Main) { printOnUi("[UDP-Client][Error] ", "No response from server within 5 seconds. Closing socket.") }
                e.printStackTrace()
            } finally {
                // Close socket
                socket.close()
                withContext(Dispatchers.Main) { printOnUi("[UDP-Client] ", "All packets sent. Socket closed.") }
            }

        } catch (e: Exception) {
            myLog(type = "error", msg = "[UDP-Client] Error: ${e.message}")
            withContext(Dispatchers.Main) { printOnUi("[UDP-Client] ", "Error: ${e.message}") }
            e.printStackTrace()
        }
    }
}