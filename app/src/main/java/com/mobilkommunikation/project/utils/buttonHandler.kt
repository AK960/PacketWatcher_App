package com.mobilkommunikation.project.utils

import com.mobilkommunikation.project.model.ServerViewModel
import com.mobilkommunikation.project.service.tcp.startTcpClient
import com.mobilkommunikation.project.service.udp.startUdpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun handleStartClientInteraction(
    ipAddress: String,
    portNumber: Int,
    tcpMessage: String,
    protocolSelected: String,
    viewModel: ServerViewModel
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