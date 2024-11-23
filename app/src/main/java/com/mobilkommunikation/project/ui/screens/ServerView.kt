package com.mobilkommunikation.project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.ui.components.OutputField
import com.mobilkommunikation.project.ui.components.ServerInputFields

@Composable
fun PacketWatcherServerView(
    // parameters to come
) {
    var ipAddress by rememberSaveable { mutableStateOf("") }
    val messages = rememberSaveable() { mutableStateListOf<String>() }

    Column {
        ServerInputFields(
            ipAddress = "",
            onIpAddressChange = { ipAddress = it },
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutputField(outputText = messages.joinToString("\n") { it })
    }
    fun receiveMessage(message: String) {
        messages.add(0, message)
    }
    receiveMessage(message = "This is a manually generated message.")
}

