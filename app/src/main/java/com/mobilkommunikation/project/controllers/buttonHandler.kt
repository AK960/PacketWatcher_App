package com.mobilkommunikation.project.controllers

// Client Side
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

// Server Side
fun handleStartServerInteraction(
    ipAddress: String,
    protocolSelected: String
) {
    if (protocolSelected == "TCP") {
        myLog(msg = "serverButtonHandler: $protocolSelected-Protocol selected")
        handleStartServerRequest(ipAddress = ipAddress, protocolSelected = protocolSelected)
    } else {
        myLog(msg = "serverButtonHandler: $protocolSelected-Protocol selected")
    }
}


