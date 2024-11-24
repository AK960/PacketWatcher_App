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
import com.mobilkommunikation.project.controllers.myLog

@Composable
fun ServerButtons(
    options: List<String>,
    onClick : (String) -> Unit,
) {
    Row (
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            myLog(msg = "Rendering $option Button")
            Button(
                onClick = { onClick(option) },
                modifier = Modifier
                    .padding(4.dp)
                    .height(56.dp)
                    .weight(1f)
            ) {
                Text("Start $option Server")
            }
        }
    }
}