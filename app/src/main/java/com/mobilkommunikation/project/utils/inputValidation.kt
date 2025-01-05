package com.mobilkommunikation.project.utils

import java.net.ServerSocket

sealed class PortValidationResult {
    data object Valid : PortValidationResult()
    data object InvalidRange : PortValidationResult()
    data object Blocked : PortValidationResult()
}

fun isValidIpAddress(ipAddress: String): Boolean {
    val ipPattern = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    return ipPattern.matches(ipAddress)
}

fun isValidPortNumber(portNumber: String): PortValidationResult {
    // elvis statement: when transforming to int returns null, return valid (later getAvailablePort())
    val port = portNumber.toIntOrNull()
    if (port == null || port !in 1024..65535) {
        return PortValidationResult.InvalidRange
    }
    return try {
        val socket = ServerSocket(port)
        socket.close()
        PortValidationResult.Valid
    } catch (e: Exception) {
        PortValidationResult.Valid //here PortValidationResult.Blocked --> .Valid only for adb port forward testing
            // TODO: How to handle case when port forwarding on port 8080 and server listening on port 8080?
    }
}
