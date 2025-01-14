package com.mobilkommunikation.project.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.utils.myLog

@Composable
fun ClientInputFields(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    portNumber: String,
    onPortNumberChange: (String) -> Unit,
    transmissionMessage: String,
    onTransmissionMessageChange: (String) -> Unit,
    nPackets: String,
    onNPacketsChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = ipAddress,
            onValueChange = {
                myLog(type = "debug", msg = "Input Fields: IP-Address changed to $it")
                onIpAddressChange(it)
            },
            label = { Text("IP-Address") },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .weight(0.5f)
                .padding(4.dp)
        )
        TextField(
            value = portNumber,
            onValueChange = {
                onPortNumberChange(it)
                myLog(type = "debug", msg = "Input Fields: Port number changed to $it")
            },
            label = { Text("Port") },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .weight(0.25f)
                .padding(4.dp)
        )
        TextField(
            value = nPackets,
            onValueChange = {
                onNPacketsChange(it)
                myLog(type = "debug", msg = "Input Fields: Number of packets changed to $it")
            },
            label = { Text("Pckts") },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .weight(0.25f)
                .padding(4.dp)
        )
        myLog(msg = "Input Fields: Chosen connection parameters: $ipAddress:$portNumber")
    }
    Row (modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = transmissionMessage,
            onValueChange = {
                myLog(msg = "Input Fields: Message changed to '$it'")
                onTransmissionMessageChange(it)
            },
            label = { Text("Message") },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        )
    }
}

@Composable
fun ServerInputFields(
    portNumber: String,
    onPortNumberChange: (String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = portNumber,
            onValueChange = {
                onPortNumberChange(it)
                myLog(type = "debug", msg = "Input Fields: Port number changed to $it")
            },
            label = { Text("Port Number") },
            modifier = Modifier
                .weight(0.6f)
                .padding(4.dp)
                .height(56.dp)
                .focusable(true)
        )
    }
}
