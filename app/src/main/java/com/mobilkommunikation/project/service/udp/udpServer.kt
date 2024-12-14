package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.getAvailablePort
import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

@OptIn(DelicateCoroutinesApi::class)
fun startUdpServer(
    portNumber: Int = getAvailablePort(),
    printOnUi: (message: String, String) -> Unit
): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        val socket = DatagramSocket(portNumber)
        val serverAddress = socket.localAddress.hostAddress
        myLog(type = "debug", msg = "udpServer: Server is listening on port $portNumber in thread ${Thread.currentThread().name}.")

        launch {
            while (isActive) {
                delay(10000L)
                myLog(type = "debug", msg = "udpServer: Server is still listening on port $portNumber in thread ${Thread.currentThread().name}.")
            }
        }

        val buffer = ByteArray(1024)
        try {
            while (isActive) {
                launch {
                    try {
                        // Read Message and client details
                        val udpPacket = DatagramPacket(buffer, buffer.size)
                        socket.receive(udpPacket)
                        val clientMessage = String(udpPacket.data, 0, udpPacket.length)

                        // Get details
                        val clientAddress = udpPacket.address.hostAddress
                        myLog(msg = "udpServer: Received message from client $clientAddress: $clientMessage")

                        // Process Message
                        val serverMessage = "UDP-Server $serverAddress: Message received and processed."

                        // Return to UI
                        withContext(Dispatchers.Main) {
                            printOnUi("UDP-Client: $clientAddress", "Message: $clientMessage")
                        }

                        // Respond to Client
                        val response = serverMessage.toByteArray()
                        val responsePacket = DatagramPacket(response, response.size, udpPacket.address, udpPacket.port)
                        socket.send(responsePacket)
                        myLog(msg = "udpServer: Sent response to client $clientAddress")
                    } catch (e: Exception) {
                        myLog(type = "error", msg = "udpServer: Error receiving packet: ${e.message}")
                        e.printStackTrace()
                    } finally {
                        socket.close()
                        myLog(msg = "udpServer: Client socket closed.")
                    }
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