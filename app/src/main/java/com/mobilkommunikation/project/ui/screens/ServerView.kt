package com.mobilkommunikation.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobilkommunikation.project.model.ServerViewModel
import com.mobilkommunikation.project.ui.components.ServerButtons
import com.mobilkommunikation.project.ui.components.ServerInputFields
import com.mobilkommunikation.project.utils.PortValidationResult
import com.mobilkommunikation.project.utils.myLog
import com.mobilkommunikation.project.utils.validateServerPortNumber


@Composable
fun PacketWatcherServerView() {
    myLog(msg = "PacketWatcherServerView: Rendering Server View")
    // Viewmodel
    val serverViewModel: ServerViewModel = viewModel()
    val messages by serverViewModel.serverMessages.collectAsState()
    val tcpServerRunning by serverViewModel.tcpServerRunning.collectAsState()
    val udpServerRunning by serverViewModel.udpServerRunning.collectAsState()

    // States
    var portNumber by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            //.verticalScroll(rememberScrollState())
    ) {
        ServerInputFields(
            portNumber = portNumber,
            onPortNumberChange = { portNumber = it },
        )
        Spacer(modifier = Modifier.height(8.dp))

        ServerButtons(
            tcpServerStatus = if (tcpServerRunning) "Stop" else "Start",
            udpServerStatus = if (udpServerRunning) "Stop" else "Start",
            onClick = { option ->
                when (option) {
                    "TCP" -> {
                        if (tcpServerRunning) {
                            serverViewModel.stopTcpServer()
                            errorMessage = ""
                            myLog(msg = "PacketWatcherServerView: Stopping TCP Server")
                        } else {
                            when (validateServerPortNumber(portNumber)) {
                                PortValidationResult.Valid -> {
                                    serverViewModel.startTcpServerView(portNumber.toInt())
                                    errorMessage = ""
                                    myLog(msg = "PacketWatcherServerView: Trying to start TCP Server on port $portNumber")
                                }
                                PortValidationResult.InvalidRange -> {
                                    errorMessage = "Invalid port number. Choose a port from 1024 to 65535."
                                    myLog(type = "error", msg = "PacketWatcherServerView: $errorMessage")
                                }
                                PortValidationResult.Blocked -> {
                                    errorMessage = "Port is already in use. Choose a different port."
                                    myLog(type = "error", msg = "PacketWatcherServerView: $errorMessage")
                                }
                            }
                        }
                    }
                    "UDP" -> {
                        if (udpServerRunning) {
                            serverViewModel.stopUdpServer()
                            errorMessage = ""
                            myLog(msg = "PacketWatcherServerView: Stopping UDP Server")
                        } else {
                            when (validateServerPortNumber(portNumber)) {
                                PortValidationResult.Valid -> {
                                    serverViewModel.startUdpServerView(portNumber.toInt())
                                    errorMessage = ""
                                    myLog(msg = "PacketWatcherServerView: Trying to start UDP Server on port $portNumber")
                                }
                                PortValidationResult.InvalidRange -> {
                                    errorMessage = "Invalid port number. Choose a port from 1024 to 65535."
                                    myLog(type = "error", msg = "PacketWatcherServerView: $errorMessage")
                                }
                                PortValidationResult.Blocked -> {
                                    errorMessage = "Port is already in use. Choose a different port."
                                    myLog(type = "error", msg = "PacketWatcherServerView: $errorMessage")
                                }
                            }
                        }
                    }
                }
                portNumber = ""
            },
        )
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // ViewModel output
        Text(
            text = "Monitoring",
            modifier = Modifier
                .height(48.dp)
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(messages) { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        //OutputField(outputText = "Display Output here")
    }
}


