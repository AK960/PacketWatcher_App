package com.mobilkommunikation.project

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.mobilkommunikation.project.ui.screens.PacketWatcherClientView
import com.mobilkommunikation.project.ui.screens.PacketWatcherNetworkView
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

        if(!internetPermission()){
            requestInternetPermission()
        } else {
            Toast.makeText(this, "Internet Permission Granted", Toast.LENGTH_SHORT).show()
        }

        setContent {
            myLog(msg = "MainActivity: Starting Application ...")
            PacketWatcherAppScaffold()
        }
    }

    private fun internetPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestInternetPermission() {
        requestPermissions(arrayOf(Manifest.permission.INTERNET), INTERNET_PERMISSION_REQUEST_CODE)
    }

    companion object {
        private const val INTERNET_PERMISSION_REQUEST_CODE = 1
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PacketWatcherAppScaffold () {
    // Request permissions for network info
    val connectivityInfoPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    // Pass necessary user interaction permissions to function
    RequestPacketWatcherPermissions(connectivityInfoPermissions)

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
        BottomNavigationItem(
            title = "Network",
            selectedIcon = painterResource(id = R.drawable.icon_network),
            unselectedIcon = painterResource(id = R.drawable.icon_network),
        ),
    )
    var clientServerStateIndex by rememberSaveable { mutableIntStateOf(2) }
    var selectedProtocolStateIndex by rememberSaveable { mutableStateOf("TCP") }
    var selectedNetworkInfoIndex by rememberSaveable { mutableStateOf("Connectivity Information") }

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
                } else if (clientServerStateIndex == 1) {
                    myLog(msg = "PacketWatcherAppScaffold: Switching Tabs: Server ($clientServerStateIndex) selected")
                    PacketWatcherServerView()
                } else {
                    myLog(msg = "PacketWatcherAppScaffold: Switching Tabs: Network ($clientServerStateIndex) selected")
                    PacketWatcherNetworkView(
                        selectedNetworkInfoIndex,
                        onNetworkInfoSelected = { selectedNetworkInfoIndex = it }
                    )
                }
            }
        }
    }
}

@Composable
fun RequestPacketWatcherPermissions(connectivityInfoPermissions: Array<String>) {
    val context = LocalContext.current
    // 1) We'll use rememberLauncherForActivityResult to request multiple perms at once.
    //    This avoids the deprecated onRequestPermissionsResult approach.
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // This callback is invoked with a map of permission -> granted/denied.
        // e.g. {READ_PHONE_STATE=true, ACCESS_FINE_LOCATION=false} if user allows phone but denies location
        if (permissionsMap.values.any { !it }) {
            // If any permission was denied, show a Toast or handle accordingly
            Toast.makeText(
                context,
                "Some permissions were denied; network info may be incomplete.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            // All permissions granted
            Toast.makeText(
                context,
                "All permissions granted! Full network info available.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // 2) Check if they’re already granted
    val readPhoneStateGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_PHONE_STATE
    ) == PackageManager.PERMISSION_GRANTED

    val fineLocationGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // 3) If not all are granted, launch the permission request. You could also do this on a button click.
    LaunchedEffect(Unit) {
        if (!readPhoneStateGranted || !fineLocationGranted) {
            multiplePermissionsLauncher.launch(connectivityInfoPermissions)
        }
    }

}