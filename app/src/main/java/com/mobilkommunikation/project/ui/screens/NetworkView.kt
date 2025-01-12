package com.mobilkommunikation.project.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.utils.fetchConnectivityInfo
import com.mobilkommunikation.project.utils.fetchMobileNetworkInfo
import com.mobilkommunikation.project.utils.fetchWifiInfo
import com.mobilkommunikation.project.utils.myLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun PacketWatcherNetworkView (
    selectedNetworkInfoIndex: String = "Connectivity Information",
    onNetworkInfoSelected: (String) -> Unit
) {
    myLog(msg = "PacketWatcherNetworkView: Rendering Network View")

    // Initialize data list and context
    val dataItems = remember { mutableStateListOf<String>() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val (activeNetwork, allNetworks) = fetchConnectivityInfo(context)
        dataItems.clear()
        dataItems.add("[Active Network]")
        dataItems.add(activeNetwork)
        dataItems.add("\n[All Networks]")
        dataItems.addAll(allNetworks.map { it })
    }

    Column (
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        NetworkInfoSegments(
            options = listOf("Connectivity Information", "Wifi Network", "Mobile Network"),
            selectedOption = selectedNetworkInfoIndex,
            onOptionSelected = { selectedOption ->
                onNetworkInfoSelected(selectedOption)
                // Fetch data and add to list
                dataItems.clear()
                when(selectedOption) {
                    "Connectivity Information" -> {
                        myLog(msg = "Fetching connectivity information")
                        val (activeNetwork, allNetworks) = fetchConnectivityInfo(context)
                        dataItems.add("[Active Network]")
                        dataItems.add(activeNetwork)
                        dataItems.add("\n[All Networks]")
                        dataItems.addAll(allNetworks.map { it })
                    }
                    "Wifi Network" -> {
                        myLog(msg = "Fetching WiFi information")
                        val wifiData = fetchWifiInfo(context)
                        dataItems.addAll(wifiData)
                    }
                    "Mobile Network" -> {
                        myLog(msg = "Fetching mobile network information")
                        CoroutineScope(Dispatchers.IO).launch {
                            val mobileData = fetchMobileNetworkInfo(context)
                            // Verarbeiten der erhaltenen mobilen Netzwerkdaten
                            dataItems.addAll(mobileData)
                        }
                    }
                }
            }
        )
    }

    LazyColumn (
        modifier = Modifier,
        state = LazyListState(),
        contentPadding = PaddingValues(
            start = 32.dp,
            top = 8.dp,
            end = 32.dp,
            bottom = 8.dp
        ),
        reverseLayout = false,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(dataItems) { item ->
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
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