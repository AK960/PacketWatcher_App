package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket

fun startUdpServer(
    portNumber: Int,
    scope: CoroutineScope,
    printOnUi: (label: String, message: String) -> Unit,
    onSocketCreated: (DatagramSocket) -> Unit // Callback to set the socket reference in the ViewModel
): Job {
    return scope.launch(Dispatchers.IO) {
        var udpSocket: DatagramSocket? = null

        // 1. Try: Create DatagramSocket
        try {
            // Create DatagramSocket with reuse address option
            udpSocket = DatagramSocket(portNumber).apply {
                reuseAddress = true
            }
            onSocketCreated(udpSocket) // Set socket reference in the ViewModel

            // Logging
            withContext(Dispatchers.Main) { printOnUi("[UDP-Server] ", "Listening on ::${udpSocket.localPort}") }
            myLog(type = "debug", msg = "[UDP-Server] Server is listening on port $portNumber in thread ${Thread.currentThread().name}.")

            // Set Variables
            var refTime: Long? = null
            val buffer = ByteArray(1024)
            var nPackets = 0

            // 2. Try: Receive packets continuously
            try {
                while (true) {

                    // Read Message
                    val udpPacket = DatagramPacket(buffer, buffer.size)
                    udpSocket.receive(udpPacket)
                    val clientMessage = String(udpPacket.data, 0, udpPacket.length)
                    val clientAddress = udpPacket.address.hostAddress

                    // Calculate Inter Arrival Time
                    val recvTime = System.currentTimeMillis()
                    if(refTime == null) {
                        refTime = recvTime
                        nPackets = 1
                        myLog(msg = "[UDP-Server] First packet in connection: No IAT calculated.")
                        withContext(Dispatchers.Main) {
                            printOnUi("[UDP-Server][P#$nPackets] ", "First packet: No IAT calculated.")
                            printOnUi("[UDP-Client][P#$nPackets] ", clientMessage)
                        }
                    } else {
                        val iat = recvTime - refTime
                        refTime = recvTime
                        nPackets++

                        // Logging
                        myLog(msg = "[UDP-Server] IAT: $iat ms")
                        withContext(Dispatchers.Main) {
                            printOnUi("[UDP-Server][P#$nPackets] ", "Inter-Arrival-Time (IAT): $iat ms")
                            printOnUi("[UDP-Client][P#$nPackets] ", clientMessage)
                        }
                    }

                    // Respond to Client
                    val serverMessage = "[ACK] Messages acknowledged."
                    val response = serverMessage.toByteArray()
                    val responsePacket =
                        DatagramPacket(response, response.size, udpPacket.address, udpPacket.port)
                    udpSocket.send(responsePacket)
                    myLog(msg = "[UDP-Server] Sent response to client $clientAddress.")
                    //withContext(Dispatchers.Main) { printOnUi("[UDP-Server] ", "Sent response to client $clientAddress.") }
                }
            // 2. Catch: Error receiving packet
            } catch (e: IOException) {
                myLog(type = "error", msg = "[UDP-Server] Error receiving packet: ${e.message}")
                e.printStackTrace()
            }
        // 1. Catch: Failed to create DatagramSocket
        } catch (e: IOException) {
            myLog(type = "error", msg = "[UDP-Server] Failed to create socket: ${e.message}")
            e.printStackTrace()
        } finally {
            udpSocket?.close()
            myLog(msg = "[UDP-Server] Datagram socket closed")
            withContext(Dispatchers.Main) { printOnUi("[UDP-Server] ", "Server stopped.") }
        }
    }
}