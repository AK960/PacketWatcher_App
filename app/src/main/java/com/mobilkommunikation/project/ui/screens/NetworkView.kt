package com.mobilkommunikation.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.utils.myLog

@Composable
fun PacketWatcherNetworkView (
    selectedNetworkInfoIndex: String,
    onNetworkInfoSelected: (String) -> Unit
) {
    myLog(msg = "PacketWatcherNetworkView: Rendering Network View")

    LazyColumn (
        modifier = Modifier,
        state = LazyListState(),
        contentPadding = PaddingValues(16.dp),
        reverseLayout = false,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column (
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                NetworkInfoSegments(
                    options = listOf("Connectivity Information", "Wifi Network", "Mobile Network"),
                    selectedOption = selectedNetworkInfoIndex,
                    onOptionSelected = {
                        onNetworkInfoSelected(it)
                    }
                )
            }
        }
    }
}

@Composable
fun NetworkInfoSegments(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = { onOptionSelected(option) }
            ) {
                Text(text = option)
            }
        }
    }
}

