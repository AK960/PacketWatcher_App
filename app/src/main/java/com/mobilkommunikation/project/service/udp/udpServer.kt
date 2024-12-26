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
        try {
            // Create Socket
            udpSocket = DatagramSocket(portNumber).apply {
                reuseAddress = true
            }
            onSocketCreated(udpSocket) // Set the socket reference in the ViewModel

            // Logging
            withContext(Dispatchers.Main) { printOnUi("[UDP-Server]", "Listening on ::${udpSocket.localPort}") }
            myLog(type = "debug", msg = "[UDP-Server] Server is listening on port $portNumber in thread ${Thread.currentThread().name}.")

            val buffer = ByteArray(1024)
            while (true) {
                try {
                    // Read Message
                    val udpPacket = DatagramPacket(buffer, buffer.size)
                    udpSocket.receive(udpPacket)
                    val clientMessage = String(udpPacket.data, 0, udpPacket.length)
                    val clientAddress = udpPacket.address.hostAddress

                    // Acknowledge Message
                    val serverMessage = "Message acknowledged"

                    // Return to UI
                    withContext(Dispatchers.Main) { printOnUi("[UDP-Server][$clientAddress]", clientMessage) }

                    // Respond to Client
                    val response = serverMessage.toByteArray()
                    val responsePacket = DatagramPacket(response, response.size, udpPacket.address, udpPacket.port)
                    udpSocket.send(responsePacket)
                    myLog(msg = "[UDP-Server] Sent response to client $clientAddress")
                } catch (e: IOException) {
                    myLog(type = "error", msg = "[UDP-Server] Error receiving packet: ${e.message}")
                    e.printStackTrace()
                }
            }
        } catch (e: IOException) {
            myLog(type = "error", msg = "[UDP-Server] Failed to create socket: ${e.message}")
            e.printStackTrace()
        } finally {
            udpSocket?.close()
            myLog(msg = "[UDP-Server] Datagram socket closed")
            withContext(Dispatchers.Main) { printOnUi("[UDP-Server]", "Server stopped.") }
        }
    }
}