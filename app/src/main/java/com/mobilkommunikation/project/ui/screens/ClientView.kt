package com.mobilkommunikation.project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.controllers.handleSendButtonInteraction
import com.mobilkommunikation.project.controllers.isValidIpAddress
import com.mobilkommunikation.project.controllers.isValidPortNumber
import com.mobilkommunikation.project.controllers.myLog
import com.mobilkommunikation.project.ui.components.InputFields
import com.mobilkommunikation.project.ui.components.OutputField
import com.mobilkommunikation.project.ui.components.SegmentedControl
import com.mobilkommunikation.project.ui.components.SendButton

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

    Column {
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
                handleSendButtonInteraction(
                    ipAddress = ipAddress,
                    portNumber = portNumber,
                    tcpMessage = tcpMessage,
                    protocolSelected = selectedProtocolStateIndex
                )
                errorMessage = ""
                myLog(msg = "PacketWatcherClientView: Send Button clicked: Trying to connect to $ipAddress:$portNumber")
            } else {
                errorMessage = "Input Error: Invalid IP address or port number"
                myLog(msg = "PacketWatcherClientView: $errorMessage")
            }
        }
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutputField(outputText = "Display Output here")
    }
}

