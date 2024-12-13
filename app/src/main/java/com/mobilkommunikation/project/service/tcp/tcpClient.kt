package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog

fun startTcpClient(
    ipAddress: String,
    portNumber: String,
    tcpMessage: String,
    protocolSelected: String
) {
    myLog(msg = "tcpClient: Establishing $protocolSelected-Connection to $ipAddress:$portNumber. Sending message: $tcpMessage")
    // TODO: Implement transmission logic here with flexible protocol parameter
}