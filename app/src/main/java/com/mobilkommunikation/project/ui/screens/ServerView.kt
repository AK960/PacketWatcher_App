package com.mobilkommunikation.project.ui.screens

import MessageViewModel
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobilkommunikation.project.ui.components.ServerButtons
import com.mobilkommunikation.project.ui.components.ServerInputFields
import com.mobilkommunikation.project.utils.getAvailablePort
import com.mobilkommunikation.project.utils.handleStartServerInteraction
import com.mobilkommunikation.project.utils.handleStopServerInteraction
import com.mobilkommunikation.project.utils.myLog


@Composable
fun PacketWatcherServerView() {
    myLog(msg = "PacketWatcherServerView: Rendering Server View")
    // Viewmodel
    val viewModel: MessageViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()

    // States
    var portNumber by rememberSaveable { mutableIntStateOf(0) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var tcpServerStatus by rememberSaveable { mutableStateOf("Start") }
    var udpServerStatus by rememberSaveable { mutableStateOf("Start") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ServerInputFields(
            portNumber = portNumber,
            onPortNumberChange = { portNumber = it },
        )
        Spacer(modifier = Modifier.height(16.dp))

        ServerButtons(
            tcpServerStatus = tcpServerStatus,
            udpServerStatus = udpServerStatus,
            onClick = { option ->
                val actualPortNumber = if (portNumber == 0) getAvailablePort() else portNumber
                when (option) {
                    "TCP" -> {
                        if (tcpServerStatus == "Start") {
                            handleStartServerInteraction(
                                portNumber = actualPortNumber,
                                protocolSelected = option
                            )
                            tcpServerStatus = "Stop"
                            errorMessage = ""
                            myLog(msg = "PacketWatcherServerView: Trying to start $option Server on port $actualPortNumber")
                        } else {
                            handleStopServerInteraction(
                                protocolSelected = option
                            )
                            tcpServerStatus = "Start"
                            errorMessage = ""
                            myLog(msg = "PacketWatcherServerView: Stopping $option Server")
                        }
                    }
                    "UDP" -> {
                        if (udpServerStatus == "Start") {
                            handleStartServerInteraction(
                                portNumber = actualPortNumber,
                                protocolSelected = option
                            )
                            udpServerStatus = "Stop"
                            errorMessage = ""
                            myLog(msg = "PacketWatcherServerView: Trying to start $option Server on port $actualPortNumber")
                        } else {
                            handleStopServerInteraction(
                                protocolSelected = option
                            )
                            udpServerStatus = "Start"
                            errorMessage = ""
                            myLog(msg = "PacketWatcherServerView: Stopping $option Server")
                        }
                    }
                }
            },
        )
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
        // ViewModel Output
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


