package com.mobilkommunikation.project.controllers

fun handleSendButtonInteraction(
    ipAddress: String,
    portNumber: String,
    tcpMessage: String,
    protocolSelected: String
) {
    if (protocolSelected == "TCP") {
        setupTransmission(ipAddress, portNumber, tcpMessage, protocolSelected)

    } else setupTransmission(ipAddress, portNumber, tcpMessage, protocolSelected)
}

fun setupTransmission(
    ipAddress: String,
    portNumber: String,
    tcpMessage: String,
    protocolSelected: String
) {
    // TODO: Implement transmission logic here with flexible protocol parameter
}
