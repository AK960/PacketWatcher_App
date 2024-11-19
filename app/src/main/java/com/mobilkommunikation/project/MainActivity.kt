package com.mobilkommunikation.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PacketWatcherAppScaffold()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PacketWatcherAppScaffold () {
    val items = listOf(
        BottomNavigationItem(
            title = "Client",
            selectedIcon = painterResource(id = R.drawable.icon_client),
            unselectedIcon = painterResource(id = R.drawable.icon_client),
        ),
        BottomNavigationItem(
            title = "Server",
            selectedIcon = painterResource(id = R.drawable.icon_server),
            unselectedIcon = painterResource(id = R.drawable.icon_server),
        ),
    )
    var clientServerStateIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedProtocolStateIndex by rememberSaveable { mutableStateOf("TCP") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar (
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            text = items[clientServerStateIndex].title + "-View",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = clientServerStateIndex == index,
                            onClick = {
                                clientServerStateIndex = index
                                // navController.navigate(item.title)
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    color = if (clientServerStateIndex == index) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.secondary
                                    },
                                    fontWeight = if (clientServerStateIndex == index) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    },
                                )
                            },
                            alwaysShowLabel = true,
                            icon = {
                                Icon(
                                    painter = if (index == clientServerStateIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                        )
                    }
                }
            }
        )
        { innerPadding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // If bottomBar Index set to Client, run functions that render client view, else server view
                if (clientServerStateIndex == 0) {
                    PacketWatcherClientView(
                        selectedProtocolStateIndex, // Start-Index for SegmentedControl set to TCP
                        onProtocolSelected = { selectedProtocolStateIndex = it }
                    )
                } else {
                    PacketWatcherServerView()
                }
            }
        }
    }
}

@Composable
fun PacketWatcherClientView (
    selectedProtocolStateIndex: String,
    onProtocolSelected: (String) -> Unit // Callback Function takes value from
) {
    Column {
        SegmentedControl(
            options = listOf("TCP", "UDP"),
            selectedOption = selectedProtocolStateIndex,
            onOptionSelected = onProtocolSelected
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFields(
            ipAddress = "",
            onIpAddressChange = { /* handle IP address change */ },
            portNumber = "",
            onPortNumberChange = { /* handle port number change */ },
            payload = "",
            onPayloadChange = { /* handle payload change */ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SendButton { /*Handle send button click*/ }
        Spacer(modifier = Modifier.height(16.dp))
        OutputField(outputText = "Display Output here")
    }
}

@Composable
fun SegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row {
        options.forEach { option ->
            Button(
                onClick = { onOptionSelected(option) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (option == selectedOption) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(option)
            }
        }
    }
}

@Composable
fun InputFields(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    portNumber: String,
    onPortNumberChange: (String) -> Unit,
    payload: String,
    onPayloadChange: (String) -> Unit
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = ipAddress,
                onValueChange = onIpAddressChange,
                label = { Text("IP-Address") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            TextField(
                value = portNumber,
                onValueChange = onPortNumberChange,
                label = { Text("Port Nr.") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
    Column {
        Row (modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = payload,
                onValueChange = onPayloadChange,
                label = { Text("Payload") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun SendButton(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    ) {
        Text("Send")
    }
}

@Composable
fun OutputField(outputText: String) {
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Text(
                text = "Monitoring",
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = outputText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun PacketWatcherServerView() {
    Text(
        text = "To Do: Not yet implemented"
    )
}