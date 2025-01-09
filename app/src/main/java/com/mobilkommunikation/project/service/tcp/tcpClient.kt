package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.Socket
import java.net.SocketTimeoutException

suspend fun startTcpClient(
    ipAddress: String,
    portNumber: Int,
    nPackets: Int,
    tcpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    withContext(Dispatchers.IO){
        myLog(msg = "[TCP-Client] Connecting to $ipAddress:$portNumber.")
        withContext(Dispatchers.Main) { printOnUi("[TCP-Client] ", "Connecting to $ipAddress:$portNumber.") }

        // Create Socket
        val socket = Socket(InetAddress.getByName(ipAddress), portNumber)
        socket.soTimeout = 5000

        try {
            var refTime: Long? = null

            for (i in 1..nPackets) {
                val sendTime = System.currentTimeMillis()

                if (refTime == null) {
                    refTime = sendTime
                    myLog(msg = "[TCP-Client] First packet in stream: No IAT calculated.")
                    withContext(Dispatchers.Main) { printOnUi("[TCP-Client][P#$i] ", "First packet: No IAT calculated.") }
                    val packetMessage = "[P#$i] $tcpMessage"
                    socket.getOutputStream().write((packetMessage).toByteArray())
                } else {
                    val iat = sendTime - refTime
                    refTime = sendTime
                    myLog(msg = "[TCP-Client] IAT: $iat ms")
                    withContext(Dispatchers.Main) { printOnUi("[TCP-Client][P#$i] ", "Inter-Arrival-Time (IAT): $iat ms") }
                    val packetMessage = "[P#$i] $tcpMessage"
                    socket.getOutputStream().write((packetMessage).toByteArray())
                }

                // delay 1s
                delay(1000)
            }

            // Receive response
            try {
                val tcpResponse = socket.getInputStream().bufferedReader().readLine()
                withContext(Dispatchers.Main) { printOnUi("[TCP-Server] ", tcpResponse) }
            } catch (e: SocketTimeoutException) {
                myLog(type = "error", msg = "[TCP-Client] Error receiving response: ${e.message}")
                withContext(Dispatchers.Main) { printOnUi("[TCP-Client] ", "No response from server within 5 seconds. Closing socket.") }
                e.printStackTrace()
            }

        } catch (e: Exception) {
            myLog(type = "error", msg = "[TCP-Client] Error: ${e.message}")
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client] ", "Error: ${e.message}") }
            e.printStackTrace()
        } finally {
            socket.close()
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client] ", "Everything sent. Server-connection closed.") }
        }
    }
}