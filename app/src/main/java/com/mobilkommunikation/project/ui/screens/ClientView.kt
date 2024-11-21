package com.mobilkommunikation.project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.ui.components.InputFields
import com.mobilkommunikation.project.ui.components.OutputField
import com.mobilkommunikation.project.ui.components.SegmentedControl
import com.mobilkommunikation.project.ui.components.SendButton

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

