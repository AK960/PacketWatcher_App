package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.Socket

suspend fun startTcpClient(
    ipAddress: String,
    portNumber: Int,
    nPackets: Int,
    tcpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    withContext(Dispatchers.IO){
        try {
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client] ", "Connecting to $ipAddress:$portNumber") }
            myLog(msg = "[TCP-Client] Connecting to $ipAddress:$portNumber.")
            myLog(msg = "[UDP-Client] Sending $nPackets packets to $ipAddress:$portNumber")

            // Set variables and create socket
            var prevTime = System.currentTimeMillis()
            val destAddress = InetAddress.getByName(ipAddress)
            val socket = Socket(destAddress, portNumber)

            for (i in 1..nPackets) {
                // IAT handling
                val currTime = System.currentTimeMillis()
                val deltaTime = currTime - prevTime
                prevTime = currTime

                // Build message
                val packetMessage = "[P#$i][DT:$deltaTime ms] $tcpMessage"

                // Create and send packet
                socket.getOutputStream().write((packetMessage).toByteArray())
                withContext(Dispatchers.Main) { printOnUi("[TCP-Client]", packetMessage) }

                // delay 1s
                delay(1000)
            }

            // Receive response
            val tcpResponse = socket.getInputStream().bufferedReader().readLine()

            // Log response to UI
            withContext(Dispatchers.Main) { printOnUi("[TCP-Server][${socket.inetAddress.address}] ", tcpResponse) }

            // Close connection
            socket.close()
        } catch (e: Exception) {
            myLog(type = "error", msg = "[TCP-Client] Error: ${e.message}")
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client] ", "Error: ${e.message}") }
            e.printStackTrace()
        }
    }
}