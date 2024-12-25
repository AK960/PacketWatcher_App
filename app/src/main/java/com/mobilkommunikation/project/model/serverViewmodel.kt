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
import java.net.ServerSocket

class ServerViewModel : ViewModel() {
    // Monitor list with messages
    private val _serverMessages = MutableStateFlow<List<String>>(emptyList())
    val serverMessages = _serverMessages.asStateFlow()

    // Initialize jobs
    private var tcpServerJob: Job? = null
    private var udpServerJob: Job? = null

    // States of servers
    private val _tcpServerRunning = MutableStateFlow(false)
    val tcpServerRunning = _tcpServerRunning.asStateFlow()

    private val _udpServerRunning = MutableStateFlow(false)
    val udpServerRunning = _udpServerRunning.asStateFlow()

    // Reference to server socket to close when cancelling job
    private var serverSocket: ServerSocket? = null

    // Append message to list
    fun addMessage(clientInfo: String, message: String) {
        val formattedMessage = "$clientInfo $message"
        _serverMessages.update { currentList ->
            listOf(formattedMessage) + currentList
        }
    }

    // Logic to start and stop servers
    fun startTcpServerView(portNumber: Int) {
        try {
            tcpServerJob = startTcpServer(portNumber = portNumber, scope = viewModelScope) { client, message ->
                viewModelScope.launch {
                    addMessage(client, message)
                }
            }
            _tcpServerRunning.value = true
        } catch (e: Exception) {
            myLog(type = "error", msg = "ServerViewModel: Failed to start server. Exit with error: ${e.message}")
            addMessage("[TCP-Server]", "Error: ${e.message}")
            e.printStackTrace()
        }
    }

    fun stopTcpServer() {
        tcpServerJob?.cancel()
        tcpServerJob = null
        _tcpServerRunning.value = false

        serverSocket?.close()
        serverSocket = null

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
