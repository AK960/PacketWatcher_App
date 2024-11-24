package com.mobilkommunikation.project.ui.screens

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.controllers.handleStartServerInteraction
import com.mobilkommunikation.project.controllers.isValidIpAddress
import com.mobilkommunikation.project.controllers.myLog
import com.mobilkommunikation.project.ui.components.OutputField
import com.mobilkommunikation.project.ui.components.ServerButtons
import com.mobilkommunikation.project.ui.components.ServerInputFields

@Composable
fun PacketWatcherServerView(

) {
    myLog(msg = "PacketWatcherServerView: Rendering Server View")
    var ipAddress by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ServerInputFields(
            ipAddress = "",
            onIpAddressChange = { ipAddress = it },
        )
        Spacer(modifier = Modifier.height(16.dp))

        ServerButtons(
            options = listOf("TCP", "UDP"),
            onClick = { option ->
                if (isValidIpAddress(ipAddress)) {
                    handleStartServerInteraction(
                        ipAddress = ipAddress,
                        protocolSelected = option
                    )
                    errorMessage = ""
                    myLog(msg = "PacketWatcherServerView: Trying to start $option Server on $ipAddress")
                } else {
                    errorMessage = "Input Error: Invalid IP address"
                    myLog(type = "error", msg = "PacketWatcherServerView: $errorMessage")
                }

            }
        )
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


