package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.getAvailablePort
import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

fun startUdpServer(
    portNumber: Int = getAvailablePort(),
    scope: CoroutineScope,
    printOnUi: (message: String, String) -> Unit
): Job {
    return scope.launch(Dispatchers.IO) {
        try {
            // Create Socket
            val socket = DatagramSocket(portNumber)

            // Logging
            withContext(Dispatchers.Main) { printOnUi("[UDP-Server]", "Server listening on port ::$portNumber") }
            myLog(type = "debug", msg = "udpServer: Server is listening on port $portNumber in thread ${Thread.currentThread().name}.")
            launch {
                while (isActive) {
                    delay(10000L)
                    myLog(type = "debug", msg = "udpServer: Server is still listening on port $portNumber in thread ${Thread.currentThread().name}.")
                }
            }

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
                    val serverMessage = "UDP-Server: Message acknowledged."

                    // Return to UI
                    withContext(Dispatchers.Main) { printOnUi("UDP-Client: $clientAddress", "Message: $clientMessage") }

                    // Respond to Client
                    val response = serverMessage.toByteArray()
                    val responsePacket = DatagramPacket(response, response.size, udpPacket.address, udpPacket.port)
                    socket.send(responsePacket)
                    myLog(msg = "udpServer: Sent response to client $clientAddress")
                }
            } catch (e: Exception) {
                myLog(type = "error", msg = "udpServer: Failed to start server. Exit with error: ${e.message}")
                e.printStackTrace()
            } finally {
                socket.close()
                withContext(Dispatchers.Main) { printOnUi("UDP-Server", "Datagram-Socket closed.") }
            }
        } catch (e: Exception) {
            myLog(type = "error", msg = "udpServer: Failed to create socket. Exit with error: ${e.message}")
            e.printStackTrace()
        } finally {
            withContext(Dispatchers.Main) { printOnUi("UDP-Server", "Server stopped.") }
        }
    }
}