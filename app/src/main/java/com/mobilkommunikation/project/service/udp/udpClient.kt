package com.mobilkommunikation.project.service.udp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun startUdpClient(
    ipAddress: String,
    portNumber: Int,
    udpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    return withContext(Dispatchers.IO) {
        myLog(msg = "udpClient: Sending UDP-packet to $ipAddress:$portNumber. Sending message: $udpMessage")

    }
    // TODO: Implement transmission logic here with flexible protocol parameter
}