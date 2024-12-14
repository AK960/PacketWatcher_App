package com.mobilkommunikation.project.ui.screens

import MessageViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.ui.components.InputFields
import com.mobilkommunikation.project.ui.components.OutputField
import com.mobilkommunikation.project.ui.components.SegmentedControl
import com.mobilkommunikation.project.ui.components.SendButton
import com.mobilkommunikation.project.utils.handleStartClientInteraction
import com.mobilkommunikation.project.utils.isValidIpAddress
import com.mobilkommunikation.project.utils.isValidPortNumber
import com.mobilkommunikation.project.utils.myLog

@Composable
fun PacketWatcherClientView (
    selectedProtocolStateIndex: String,
    onProtocolSelected: (String) -> Unit
) {
    myLog(msg = "PacketWatcherClientView: Rendering Client View")
    var ipAddress by rememberSaveable { mutableStateOf("") }
    var portNumber by rememberSaveable { mutableStateOf("") }
    var tcpMessage by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val viewModel = MessageViewModel()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SegmentedControl(
            options = listOf("TCP", "UDP"),
            selectedOption = selectedProtocolStateIndex,
            onOptionSelected = onProtocolSelected
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFields(
            ipAddress = "",
            onIpAddressChange = { ipAddress = it },
            portNumber = "",
            onPortNumberChange = { portNumber = it },
            tcpMessage = "",
            onTcpMessageChange = { tcpMessage = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SendButton {
            if (isValidIpAddress(ipAddress) && isValidPortNumber(portNumber)) {
                handleStartClientInteraction(
                    ipAddress = ipAddress,
                    portNumber = portNumber.toInt(),
                    tcpMessage = tcpMessage,
                    protocolSelected = selectedProtocolStateIndex,
                    viewModel = viewModel
                )
                errorMessage = ""
                myLog(msg = "PacketWatcherClientView: Send Button clicked: Trying to connect to $ipAddress:$portNumber")
            } else {
                errorMessage = "Input Error: Invalid IP address or port number"
                myLog(type = "error", msg = "PacketWatcherClientView: $errorMessage")
            }
        }
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
        OutputField(outputText = "Display Output here")
    }
}

