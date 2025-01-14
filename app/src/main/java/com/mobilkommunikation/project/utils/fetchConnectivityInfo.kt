package com.mobilkommunikation.project.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

// ConnectivityManager
fun fetchConnectivityInfo(
    context: Context
): Pair<String, List<String>> {
    // Get permissions for fetching connectivity info
    val canReadPhoneState = checkPermission(context, Manifest.permission.READ_PHONE_STATE)
    val canAccessFineLocation = checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

    // Check permissions for fetching connectivity info
    if (canReadPhoneState || canAccessFineLocation) {

        // Initialize connectivity manager
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get active network info
        val activeNetwork = connectivityManager.activeNetwork
        val activeCapabilities = activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
        val activeNetworkType = when {
            activeNetwork == null -> "No active network"
            activeCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "WiFi"
            activeCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Cellular"
            activeCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> "Ethernet"
            else -> "Unknown"
        }

        // Get all networks info using Callback
        val allNetworksDetails = mutableListOf<String>()
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val caps = connectivityManager.getNetworkCapabilities(network)
                if (caps != null) {
                    val networkType = when {
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
                        caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                        else -> "Unknown"
                    }
                    allNetworksDetails.add(networkType)
                }
            }
        }

        // Register the NetworkCallback
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)

        // Wait for callback to collect data
        Thread.sleep(100)

        // Unregister the callback to prevent memory leaks
        connectivityManager.unregisterNetworkCallback(networkCallback)

        // Return both active network and all network details
        return activeNetworkType to allNetworksDetails

    } else {
        myLog(msg = "ConnectivityManager: Permissions not granted!")
        return Pair("Permission Denied: READ_PHONE_STATE or ACCESS_FINE_LOCATION.", emptyList())
    }
}


/* WifiManager
fun fetchWifiInfo(
    context: Context
): Pair<String, List<String>> {
    // Get permissions for fetching wifi info
    val canAccessWifiState = checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)

    // Initialize wifi manager
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

}

// TelephonyManager
fun fetchMobileNetworkInfo(
    context: Context
): Pair<String, List<String>> {
    // Get permissions for fetching mobile network info
    val canAccessNetworkState = checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)

    // Initialize telephony manager
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    return
}


*/