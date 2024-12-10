package com.mobilkommunikation.project.service.tcp

import com.mobilkommunikation.project.utils.myLog

fun setupTransmission(
    ipAddress: String,
    portNumber: String,
    tcpMessage: String,
    protocolSelected: String
) {
    myLog(msg = "tcpClient: Establishing $protocolSelected-Connection to $ipAddress:$portNumber with message: $tcpMessage")
    // TODO: Implement transmission logic here with flexible protocol parameter
}