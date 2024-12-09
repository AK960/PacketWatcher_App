package com.mobilkommunikation.project.utils

import com.mobilkommunikation.project.service.tcp.setupTransmission
import com.mobilkommunikation.project.service.tcp.startTcpServer
import com.mobilkommunikation.project.service.udp.startUdpServer

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
    myLog(msg = "serverButtonHandler: $protocolSelected-Protocol selected")
    when (protocolSelected) {
        "TCP" -> startTcpServer(ipAddress)
        "UDP" -> startUdpServer(ipAddress)
    }
}


