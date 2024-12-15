package com.mobilkommunikation.project.ui.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.utils.myLog

@Composable
fun ServerInputFields(
    portNumber: String,
    onPortNumberChange: (String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = portNumber,
            onValueChange = {
                onPortNumberChange(it)
                myLog(type = "debug", msg = "Input Fields: Port number changed to $it")
            },
            label = { Text("Port Number") },
            modifier = Modifier
                .weight(0.6f)
                .padding(4.dp)
                .height(56.dp)
                .focusable(true)
        )
    }
}
