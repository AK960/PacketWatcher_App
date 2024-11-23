package com.mobilkommunikation.project.controllers

fun handleSendButtonInteraction(
    ipAddress: String,
    portNumber: String,
    tcpMessage: String,
    protocolSelected: String
) {
    if (protocolSelected == "TCP") {
        myLog(msg = "clientButtonHandler: $protocolSelected-Protocol selected")
        setupTransmission(ipAddress, portNumber, tcpMessage, protocolSelected)
    } else {
        myLog(msg = "clientButtonHandler: $protocolSelected-Protocol selected")
        setupTransmission(ipAddress, portNumber, tcpMessage, protocolSelected)
    }
}


