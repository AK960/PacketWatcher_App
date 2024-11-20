package com.mobilkommunikation.project.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputFields(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    portNumber: String,
    onPortNumberChange: (String) -> Unit,
    payload: String,
    onPayloadChange: (String) -> Unit
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = ipAddress,
                onValueChange = onIpAddressChange,
                label = { Text("IP-Address") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            TextField(
                value = portNumber,
                onValueChange = onPortNumberChange,
                label = { Text("Port Nr.") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
    Column {
        Row (modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = payload,
                onValueChange = onPayloadChange,
                label = { Text("Payload") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
}