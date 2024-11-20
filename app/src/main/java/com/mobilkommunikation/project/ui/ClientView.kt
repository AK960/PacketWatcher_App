package com.mobilkommunikation.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PacketWatcherClientView (
    selectedProtocolStateIndex: String,
    onProtocolSelected: (String) -> Unit
) {
    Column {
        SegmentedControl(
            options = listOf("TCP", "UDP"),
            selectedOption = selectedProtocolStateIndex,
            onOptionSelected = onProtocolSelected
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFields(
            ipAddress = "",
            onIpAddressChange = { /* handle IP address change */ },
            portNumber = "",
            onPortNumberChange = { /* handle port number change */ },
            payload = "",
            onPayloadChange = { /* handle payload change */ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SendButton { /*Handle send button click*/ }
        Spacer(modifier = Modifier.height(16.dp))
        OutputField(outputText = "Display Output here")
    }
}

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

@Composable
fun SendButton(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    ) {
        Text("Send")
    }
}

@Composable
fun OutputField(outputText: String) {
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
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
    }
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = outputText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}