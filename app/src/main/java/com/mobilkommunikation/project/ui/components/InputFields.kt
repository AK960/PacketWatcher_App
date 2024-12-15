package com.mobilkommunikation.project.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.utils.myLog

@Composable
fun InputFields(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    portNumber: String,
    onPortNumberChange: (String) -> Unit,
    tcpMessage: String,
    onTcpMessageChange: (String) -> Unit
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
                .weight(1f)
                .padding(4.dp)
                .focusable(true)
        )
        TextField(
            value = portNumber,
            onValueChange = {
                onPortNumberChange(it)
                myLog(type = "debug", msg = "Input Fields: Port number changed to $it")
            },
            label = { Text("Port Nr.") },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .focusable(true)
        )
        myLog(msg = "Input Fields: Chosen connection parameters: $ipAddress:$portNumber")
    }
    Row (modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = tcpMessage,
            onValueChange = {
                myLog(msg = "Input Fields: Message changed to '$it'")
                onTcpMessageChange(it)
            },
            label = { Text("Message") },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .focusable(true)
        )
}
}
