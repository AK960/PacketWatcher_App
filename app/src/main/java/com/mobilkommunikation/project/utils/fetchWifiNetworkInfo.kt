package com.mobilkommunikation.project.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager

@SuppressLint("HardwareIds")
fun fetchWifiInfo(
    context: Context
): List<String> {
    // Initialize return list
    val wifiInfoList = mutableListOf<String>()
    // Get permissions for fetching wifi info
    val canAccessWifiState = checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)

    if (canAccessWifiState){
        // Initialize wifi manager
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Fetch wifi info
        // Connection Info
        val wifiInfo = wifiManager.connectionInfo
        wifiInfoList.add("[Connection Info]")
        wifiInfoList.add("IP Address: ${makeValidIpAddress(wifiInfo.ipAddress)}")
        wifiInfoList.add("MAC Address: ${wifiInfo.macAddress}")
        wifiInfoList.add("Link Speed: ${wifiInfo.linkSpeed} Mbps")
        wifiInfoList.add("SSID: ${wifiInfo.ssid}")
        wifiInfoList.add("BSSID: ${wifiInfo.bssid}")
        wifiInfoList.add("RSSI: ${wifiInfo.rssi} dBm")

        // DHCP Info
        val dhcp = wifiManager.dhcpInfo
        wifiInfoList.add("\n[DHCP Info]")
        val dns1 = dhcp.dns1
        val dns2 = dhcp.dns2
        if(dhcp.dns1 != 0){ wifiInfoList.add("DNS1: ${makeValidIpAddress(dns1)}") } else { wifiInfoList.add("DNS1: None") }
        if(dhcp.dns2 != 0){ wifiInfoList.add("DNS2: ${makeValidIpAddress(dns2)}") } else { wifiInfoList.add("DNS2: None") }

        // Configured Networks
        wifiInfoList.add("\n[Configured Networks]")
        val configuredNetworks = wifiManager.configuredNetworks
        if (configuredNetworks != null) {
            if (configuredNetworks.isNotEmpty()) {
                configuredNetworks.forEach { network ->
                    wifiInfoList.add("Network ID: ${network.networkId}")
                    wifiInfoList.add("SSID: ${network.SSID}")
                    wifiInfoList.add("BSSID: ${network.BSSID}")
                    wifiInfoList.add("Priority: ${network.priority}")
                }
            } else {
                wifiInfoList.add("No configured networks found.")
            }
        } else {
            wifiInfoList.add("Configured networks is null.")
        }

    } else {
        wifiInfoList.add("Permission Denied: ACCESS_WIFI_STATE.")
    }
    return wifiInfoList
}
