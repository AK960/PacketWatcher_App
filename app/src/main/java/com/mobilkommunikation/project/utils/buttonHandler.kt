package com.mobilkommunikation.project.utils

import MessageViewModel
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
    protocolSelected: String,
    viewModel: MessageViewModel
) {
    myLog(type = "debug", msg = "handleStartClientInteraction: Calling $protocolSelected-Client from thread ${Thread.currentThread().name}.")

    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch {
        when (protocolSelected) {
            "TCP" -> startTcpClient(ipAddress, portNumber, tcpMessage) { client, message ->
                viewModel.addMessage(client, message)
            }
            "UDP" -> startUdpClient(ipAddress, portNumber, tcpMessage) { client, message ->
                viewModel.addMessage(client, message)
            }
        }
    }
}

fun handleStartServerInteraction(
    portNumber: Int,
    protocolSelected: String,
    viewModel: MessageViewModel
) {
    myLog(type = "debug", msg = "handleStartServerInteraction: Calling $protocolSelected-Server from thread ${Thread.currentThread().name}.")
    when (protocolSelected) {
        "TCP" -> tcpServerJob = startTcpServer(portNumber = portNumber) { client, message ->
            viewModel.addMessage(client, message)
        }
        "UDP" -> udpServerJob = startUdpServer(portNumber = portNumber) { client, message ->
            viewModel.addMessage(client, message)
        }
    }
}

fun handleStopServerInteraction(
    protocolSelected: String,
    viewModel: MessageViewModel
) {
    when (protocolSelected) {
        "TCP" -> {
            tcpServerJob?.cancel()
            tcpServerJob = null
            myLog(msg = "handleStopServerInteraction: TCP Server stopped.")
            viewModel.addMessage("TCP-Server", "Server stopped.")
        }
        "UDP" -> {
            udpServerJob?.cancel()
            udpServerJob = null
            myLog(msg = "handleStopServerInteraction: UDP Server stopped.")
            viewModel.addMessage("UDP-Server", "Server stopped.")
        }
    }
}