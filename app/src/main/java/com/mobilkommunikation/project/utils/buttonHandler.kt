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

suspend fun handleStartServerInteraction(
    portNumber: Int,
    protocolSelected: String
) {
    myLog(msg = "handleStartServerInteraction: $protocolSelected-Protocol selected")
    myLog(type = "debug", msg = "handleStartServerInteraction: Calling server from thread ${Thread.currentThread().name}.")
    when (protocolSelected) {
        "TCP" -> tcpServerJob = startTcpServer(portNumber = portNumber) { _, message ->
            //TODO: Implement logic for returning messages to UI
        }
        "UDP" -> udpServerJob = startUdpServer(portNumber = portNumber) { _, message ->
            //TODO: Implement logic for returning messages to UI
        }
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