package com.mobilkommunikation.project.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Client Side
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

// Server Side
@Composable
fun ServerButtons(
    tcpServerStatus: String,
    udpServerStatus: String,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { onClick("TCP") },
            modifier = Modifier
                .padding(4.dp)
                .height(56.dp)
                .weight(1f)
        ) {
            Text("$tcpServerStatus TCP Server")
        }
        Button(
            onClick = { onClick("UDP") },
            modifier = Modifier
                .padding(4.dp)
                .height(56.dp)
                .weight(1f)
        ) {
            Text("$udpServerStatus UDP Server")
        }
    }
}