package com.mobilkommunikation.project.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ServerInputFields(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = ipAddress,
            onValueChange = onIpAddressChange,
            label = { Text("IP-Address") },
            modifier = Modifier
                .weight(0.6f)
                .padding(4.dp)
                .height(56.dp)
        )
    }
}
