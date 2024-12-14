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
    return withContext(Dispatchers.IO){
        myLog(msg = "tcpClient: Establishing TCP-connection to $ipAddress:$portNumber. Sending message: $tcpMessage")
        try {
            // Create socket and send message
            val socket = Socket(ipAddress, portNumber)
            socket.getOutputStream().write((tcpMessage + "\n").toByteArray())
            myLog(msg = "Message sent: $tcpMessage")

            // Receive response
            val tcpResponse = socket.getInputStream().bufferedReader().readLine()
            myLog(msg = "Response received: $tcpResponse")

            // Log to UI
            withContext(Dispatchers.Main) {
                printOnUi("TCP-Client: $ipAddress", "Response: $tcpResponse")
            }

            // Close connection
            socket.close()
        } catch (e: Exception) {
            myLog(type = "error", msg = "tcpClient: Error: ${e.message}")
            e.printStackTrace()
        }
    }
}