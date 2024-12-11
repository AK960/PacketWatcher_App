package com.mobilkommunikation.project.utils

import com.mobilkommunikation.project.service.tcp.setupTransmission
import com.mobilkommunikation.project.service.tcp.startTcpServer
import com.mobilkommunikation.project.service.udp.startUdpServer
import kotlinx.coroutines.Job

var tcpServerJob: Job? = null
var udpServerJob: Job? = null

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

fun handleStartServerInteraction(
    portNumber: Int,
    serverStatus: String,
    protocolSelected: String
) {
    myLog(msg = "handleStartServerInteraction: $protocolSelected-Protocol selected")
    myLog(type = "debug", msg = "handleStartServerInteraction: Calling server from thread ${Thread.currentThread().name}.")
    when (protocolSelected) {
        "TCP" -> tcpServerJob = startTcpServer(portNumber = portNumber)
        "UDP" -> udpServerJob = startUdpServer(portNumber = portNumber)
    }
}

fun handleStopServerInteraction(
    protocolSelected: String
) {
    when (protocolSelected) {
        "TCP" -> {
            tcpServerJob?.cancel()
            myLog(msg = "handleStopServerInteraction: TCP Server stopped.")
        }
        "UDP" -> {
            udpServerJob?.cancel()
            myLog(msg = "handleStopServerInteraction: UDP Server stopped.")
        }
    }
}