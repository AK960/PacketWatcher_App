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
import com.mobilkommunikation.project.model.ClientViewModel
import com.mobilkommunikation.project.ui.components.InputFields
import com.mobilkommunikation.project.ui.components.SegmentedControl
import com.mobilkommunikation.project.ui.components.SendButton
import com.mobilkommunikation.project.utils.isValidIpAddress
import com.mobilkommunikation.project.utils.myLog

@Composable
fun PacketWatcherClientView(
    selectedProtocolStateIndex: String,
    onProtocolSelected: (String) -> Unit
) {
    myLog(msg = "PacketWatcherClientView: Rendering Client View")
    val clientViewModel: ClientViewModel = viewModel()
    val messages by clientViewModel.clientMessages.collectAsState()
    var ipAddress by rememberSaveable { mutableStateOf("") }
    var portNumber by rememberSaveable { mutableStateOf("") }
    var transmissionMessage by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            //.verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            SegmentedControl(
                options = listOf("TCP", "UDP"),
                selectedOption = selectedProtocolStateIndex,
                onOptionSelected = onProtocolSelected
            )
            Spacer(modifier = Modifier.height(8.dp))
            InputFields(
                ipAddress = ipAddress,
                onIpAddressChange = { ipAddress = it },
                portNumber = portNumber,
                onPortNumberChange = { portNumber = it },
                transmissionMessage = transmissionMessage,
                onTransmissionMessageChange = { transmissionMessage = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SendButton {
                // Input validation before sending message
                if (isValidIpAddress(ipAddress)) {
                    when (selectedProtocolStateIndex) {
                        "TCP" -> {
                            clientViewModel.startTcpClientView(ipAddress, portNumber.toInt(), transmissionMessage)
                            myLog(msg = "PacketWatcherClientView: Send Button clicked: Trying to connect to $ipAddress:$portNumber")
                        }
                        "UDP" -> {
                            clientViewModel.startUdpClientView(ipAddress, portNumber.toInt(), transmissionMessage)
                            myLog(msg = "PacketWatcherClientView: Send Button clicked: Trying to send message $transmissionMessage to $ipAddress:$portNumber")
                        }
                    }
                    errorMessage = ""
                    transmissionMessage = ""
                } else {
                    errorMessage = "Invalid IP address."
                    myLog(type = "error", msg = "PacketWatcherClientView: $errorMessage")
                }
            }
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
        }

        // LazyColumn to take remaining space and be scrollable
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            items(messages) { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}