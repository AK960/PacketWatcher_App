package com.mobilkommunikation.project.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilkommunikation.project.service.tcp.startTcpClient
import com.mobilkommunikation.project.service.udp.startUdpClient
import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ClientViewModel : ViewModel() {
    // Monitor list with messages
    private val _clientMessages = MutableStateFlow<List<String>>(emptyList())
    val clientMessages = _clientMessages.asStateFlow()

    // Append message to list
    private fun addMessage(clientInfo: String, message: String) {
        val formatedMessages = "$clientInfo$message"
        _clientMessages.update { currentList ->
            listOf(formatedMessages) + currentList
        }
    }

    // Logic to start clients
    fun startTcpClientView(
        ipAddress: String,
        portNumber: Int,
        nPackets: Int,
        tcpMessage: String
    ) {
        viewModelScope.launch {
            try {
                startTcpClient(ipAddress, portNumber, nPackets, tcpMessage) { client, message ->
                    addMessage(client, message)
                }
            } catch (e: Exception) {
                myLog(type = "error", msg = "ClientViewModel: Error starting TCP Client. Exit with error: ${e.message}")
                addMessage("[TCP-Client]", "Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun startUdpClientView(
        ipAddress: String,
        portNumber: Int,
        nPackets: Int,
        tcpMessage: String
    ) {
        viewModelScope.launch {
            try {
                startUdpClient(ipAddress, portNumber, nPackets, tcpMessage) { client, message ->
                    addMessage(client, message)
                }
            } catch (e: Exception) {
                myLog(
                    type = "error",
                    msg = "ClientViewModel: Error starting UDP Client. Exit with error: ${e.message}"
                )
                addMessage("[UDP-Client]", "Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}