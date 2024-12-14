package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun startTcpClient(
    ipAddress: String,
    portNumber: Int,
    tcpMessage: String,
    printOnUi: (String, String) -> Unit
) {
    return withContext(Dispatchers.IO){
        myLog(msg = "tcpClient: Establishing TCP-connection to $ipAddress:$portNumber. Sending message: $tcpMessage")
    }
    // TODO: Implement transmission logic here with flexible protocol parameter
}