package com.mobilkommunikation.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobilkommunikation.project.ui.screens.PacketWatcherClientView
import com.mobilkommunikation.project.ui.screens.PacketWatcherServerView
import com.mobilkommunikation.project.utils.myLog

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
            myLog(msg = "MainActivity: Starting Application ...")
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

    myLog(msg = "PacketWatcherAppScaffold: Rendering Scaffold with Values ClientServerStateIndex: $clientServerStateIndex, SelectedProtocolStateIndex: $selectedProtocolStateIndex")

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
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier.size(48.dp)
                            )
                        }
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
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // If bottomBar Index set to Client, run functions that render client view, else server view
                if (clientServerStateIndex == 0) {
                    myLog(msg = "PacketWatcherAppScaffold: Switching Tabs: Client ($clientServerStateIndex) selected")
                    PacketWatcherClientView(
                        selectedProtocolStateIndex,
                        onProtocolSelected = { selectedProtocolStateIndex = it }
                    )
                } else {
                    myLog(msg = "PacketWatcherAppScaffold: Switching Tabs: Server ($clientServerStateIndex) selected")
                    PacketWatcherServerView()
                }
            }
        }
    }
}