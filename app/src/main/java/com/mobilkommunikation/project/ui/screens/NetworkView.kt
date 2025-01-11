package com.mobilkommunikation.project.ui.screens

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.utils.myLog

@Composable
fun PacketWatcherNetworkView (
    selectedNetworkInfoIndex: String,
    onNetworkInfoSelected: (String) -> Unit
) {
    myLog(msg = "PacketWatcherNetworkView: Rendering Network View")
    val dataItems = remember { mutableListOf<String>() }
    val context = LocalContext.current

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
                    onOptionSelected = { selectedOption ->
                        onNetworkInfoSelected(selectedOption)
                        // Fetch data and add to list
                        val networkInfoData = fetchData(context, selectedOption)
                        dataItems.clear()
                        dataItems.add(networkInfoData)
                    }
                )
            }
        }
        items(dataItems) { item ->
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
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

fun fetchData(context: Context, option: String): String {
    return when (option) {
        "Connectivity Information" -> getConnectivityInfo(context)
        //"WiFi Network" -> getWifiInfo(context)
        //"Mobile Network" -> getMobileNetworkInfo(context)
        else -> "Invalid Option"
    }
}

fun getConnectivityInfo(context: Context): String {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    val allNetworks = connectivityManager.allNetworks

    val activeNetworkType = activeNetwork?.typeName ?: "No active network"
    val allNetworkTypes = allNetworks.joinToString(", ") { network ->
        connectivityManager.getNetworkInfo(network)?.typeName ?: "Unknown"
    }

    return "Active Network Type: $activeNetworkType\nAll Network Types: $allNetworkTypes"
}
