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

class ServerViewModel : ViewModel() {
    // Monitor list with messages
    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages = _messages.asStateFlow()

    // Initialize jobs
    private var tcpServerJob: Job? = null
    private var udpServerJob: Job? = null

    // States of servers
    private val _tcpServerRunning = MutableStateFlow(false)
    val tcpServerRunning = _tcpServerRunning.asStateFlow()

    private val _udpServerRunning = MutableStateFlow(false)
    val udpServerRunning = _udpServerRunning.asStateFlow()

    // Append message to list
    fun addMessage(clientInfo: String, message: String) {
        val formattedMessage = "$clientInfo $message"
        _messages.update { currentList ->
            listOf(formattedMessage) + currentList
        }
    }

    // Logic to start and stop servers
    fun startTcpServerView(portNumber: Int) {
        tcpServerJob = startTcpServer(portNumber = portNumber, scope = viewModelScope) { client, message ->
            viewModelScope.launch {
                addMessage(client, message)
            }
        }
        _tcpServerRunning.value = true
    }

    fun stopTcpServer() {
        tcpServerJob?.cancel()
        tcpServerJob = null
        _tcpServerRunning.value = false
        myLog(msg = "handleStopServerInteraction: TCP Server stopped.")
        addMessage("[TCP-Server]", "Server stopped.")
    }

    fun startUdpServerView(portNumber: Int) {
        udpServerJob = startUdpServer(portNumber = portNumber, scope = viewModelScope) { client, message ->
            viewModelScope.launch {
                addMessage(client, message)
            }
        }
        _udpServerRunning.value = true
    }

    fun stopUdpServer() {
        udpServerJob?.cancel()
        udpServerJob = null
        _udpServerRunning.value = false
        myLog(msg = "handleStopServerInteraction: UDP Server stopped.")
        addMessage("[UDP-Server]", "Server stopped.")
    }

}
