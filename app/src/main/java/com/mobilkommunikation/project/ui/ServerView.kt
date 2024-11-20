package com.mobilkommunikation.project.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.ui.components.OutputField

@Composable
fun PacketWatcherServerView(
    // parameters to come
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        OutputField(outputText = "Display Output here")
    }
}