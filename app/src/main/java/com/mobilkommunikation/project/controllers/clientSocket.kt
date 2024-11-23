package com.mobilkommunikation.project.controllers

fun setupTransmission(
    ipAddress: String,
    portNumber: String,
    tcpMessage: String,
    protocolSelected: String
) {
    myLog(msg = "clientSocket: Establishing $protocolSelected-Connection to $ipAddress:$portNumber with message: $tcpMessage")
    // TODO: Implement transmission logic here with flexible protocol parameter
}