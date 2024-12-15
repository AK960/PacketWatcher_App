package com.mobilkommunikation.project.utils

import java.net.ServerSocket

fun isValidIpAddress(ipAddress: String): Boolean {
    val ipPattern = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    return ipPattern.matches(ipAddress)
}

fun isValidPortNumber(portNumber: String): Boolean {
    val port = portNumber.toIntOrNull()
    if (port == null || port !in 1024..65535) {
        return false
    }
    return try {
        ServerSocket(port).close()
        true
    } catch (e: Exception) {
        false
    }
}