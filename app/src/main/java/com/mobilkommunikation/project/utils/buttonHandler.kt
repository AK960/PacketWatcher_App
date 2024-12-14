package com.mobilkommunikation.project.utils

import com.mobilkommunikation.project.service.tcp.startTcpClient
import com.mobilkommunikation.project.service.tcp.startTcpServer
import com.mobilkommunikation.project.service.udp.startUdpClient
import com.mobilkommunikation.project.service.udp.startUdpServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

var tcpServerJob: Job? = null
var udpServerJob: Job? = null

fun handleStartClientInteraction(
    ipAddress: String,
    portNumber: Int,
    tcpMessage: String,
    protocolSelected: String
) {
    myLog(type = "debug", msg = "handleStartClientInteraction: Calling $protocolSelected-Client from thread ${Thread.currentThread().name}.")

    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch {
        when (protocolSelected) {
            "TCP" -> startTcpClient(ipAddress, portNumber, tcpMessage) { _, _ ->
                //TODO: Implement logic for returning messages to UI
            }
            "UDP" -> startUdpClient(ipAddress, portNumber, tcpMessage) { _, _ ->
                //TODO: Implement logic for returning messages to UI
            }
        }
    }
}

fun handleStartServerInteraction(
    portNumber: Int,
    protocolSelected: String
) {
    myLog(type = "debug", msg = "handleStartServerInteraction: Calling $protocolSelected-Server from thread ${Thread.currentThread().name}.")
    when (protocolSelected) {
        "TCP" -> tcpServerJob = startTcpServer(portNumber = portNumber) { _, _ ->
            //TODO: Implement logic for returning messages to UI
        }
        "UDP" -> udpServerJob = startUdpServer(portNumber = portNumber) { _, _ ->
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