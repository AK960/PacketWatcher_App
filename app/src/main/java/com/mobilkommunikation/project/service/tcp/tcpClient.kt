package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket

suspend fun startTcpClient(
    ipAddress: String,
    portNumber: Int,
    tcpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    withContext(Dispatchers.IO){
        try {
            // Create socket
            val socket = Socket(ipAddress, portNumber)
            myLog(msg = "[TCP-Client] Connecting to $ipAddress:$portNumber.")
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client]", "Connecting to $ipAddress:$portNumber") }

            // Send message
            socket.getOutputStream().write((tcpMessage + "\n").toByteArray())
            myLog(msg = "[TCP-Client] Message sent to server")
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client]", "Message sent to server") }

            // Receive response
            val tcpResponse = socket.getInputStream().bufferedReader().readLine()
            myLog(msg = "[TCP-Client] Response from server: $tcpResponse")

            // Log response to UI
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client][${socket.inetAddress.address}]", tcpResponse) }

            // Close connection
            socket.close()
        } catch (e: Exception) {
            myLog(type = "error", msg = "[TCP-Client] Error: ${e.message}")
            withContext(Dispatchers.Main) { printOnUi("[TCP-Client]", "Error: ${e.message}") }
            e.printStackTrace()
        }
    }
}