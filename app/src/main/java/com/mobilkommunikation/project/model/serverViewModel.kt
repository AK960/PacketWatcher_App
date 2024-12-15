package com.mobilkommunikation.project.model
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilkommunikation.project.service.tcp.startTcpServer
import com.mobilkommunikation.project.service.udp.startUdpServer
import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    // Monitor list with messages
    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages = _messages.asStateFlow()

    // Initialize Jobs
    private var tcpServerJob: Job? = null
    private var udpServerJob: Job? = null

    // Append message to list
    fun addMessage(clientInfo: String, message: String) {
        val formattedMessage = "$clientInfo $message"
        _messages.update { currentList ->
            listOf(formattedMessage) + currentList
        }
    }

    fun startTcpServerView(portNumber: Int) {
        tcpServerJob = startTcpServer(portNumber = portNumber, scope = viewModelScope) { client, message ->
            viewModelScope.launch {
                addMessage(client, message)
            }
        }
    }

    fun stopTcpServer() {
        tcpServerJob?.cancel()
        tcpServerJob = null
        myLog(msg = "handleStopServerInteraction: TCP Server stopped.")
        addMessage("[TCP-Server]", "Server stopped.")
    }

    fun startUdpServerView(portNumber: Int) {
        udpServerJob = startUdpServer(portNumber = portNumber, scope = viewModelScope) { client, message ->
            viewModelScope.launch {
                addMessage(client, message)
            }
        }
    }

    fun stopUdpServer() {
        udpServerJob?.cancel()
        udpServerJob = null
        myLog(msg = "handleStopServerInteraction: UDP Server stopped.")
        addMessage("[UDP-Server]", "Server stopped.")
    }

}
