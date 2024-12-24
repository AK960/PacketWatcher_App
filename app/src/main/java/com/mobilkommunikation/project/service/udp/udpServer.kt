package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

fun startUdpServer(
    portNumber: Int,
    scope: CoroutineScope,
    printOnUi: (message: String, String) -> Unit
): Job {
    return scope.launch(Dispatchers.IO) {
        try {
            // Create Socket
            val socket = DatagramSocket(portNumber)

            // Logging
            withContext(Dispatchers.Main) { printOnUi("[UDP-Server]", "Listening on ::${socket.localPort}") }
            myLog(type = "debug", msg = "[UDP-Server] Server is listening on port $portNumber in thread ${Thread.currentThread().name}.")

            val buffer = ByteArray(1024)
            try {
                // Start Server
                while (true) {
                    // Read Message
                    val udpPacket = DatagramPacket(buffer, buffer.size)
                    socket.receive(udpPacket)
                    val clientMessage = String(udpPacket.data, 0, udpPacket.length)
                    val clientAddress = udpPacket.address.hostAddress

                    // Acknowledge Message
                    val serverMessage = "Message acknowledged"

                    // Return to UI
                    withContext(Dispatchers.Main) { printOnUi("[UDP-Server][$clientAddress]", clientMessage) }

                    // Respond to Client
                    val response = serverMessage.toByteArray()
                    val responsePacket = DatagramPacket(response, response.size, udpPacket.address, udpPacket.port)
                    socket.send(responsePacket)
                    myLog(msg = "[UDP-Server] Sent response to client $clientAddress")
                }
            } catch (e: Exception) {
                myLog(type = "error", msg = "[UDP-Server] Failed to start server. Exit with error: ${e.message}")
                e.printStackTrace()
            } finally {
                socket.close()
                myLog(msg = "[UDP-Server] Datagram socket closed")
            }
        } catch (e: Exception) {
            myLog(type = "error", msg = "[UDP-Server] Failed to create socket. Exit with error: ${e.message}")
            e.printStackTrace()
        } finally {
            withContext(Dispatchers.Main) { printOnUi("[UDP-Server]", "Server stopped.") }
        }
    }
}