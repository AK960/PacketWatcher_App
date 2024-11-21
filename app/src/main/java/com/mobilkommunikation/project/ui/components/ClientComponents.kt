package com.mobilkommunikation.project.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row {
        options.forEach { option ->
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (option == selectedOption) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.weight(1f),
                onClick = { onOptionSelected(option) }
                    // executes { selectedProtocolStateIndex = it } and thus sets the
                    // state to the option (protocol) that is clicked upon
                    // --> recomposition of SegmentedControl
            ) {
                Text(option)
            }
        }
    }
}

@Composable
fun InputFields(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    portNumber: String,
    onPortNumberChange: (String) -> Unit,
    tcpMessage: String,
    onTcpMessageChange: (String) -> Unit
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
                value = tcpMessage,
                onValueChange = onTcpMessageChange,
                label = { Text("Payload") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun SendButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    ) {
        Text("Send")
    }
}