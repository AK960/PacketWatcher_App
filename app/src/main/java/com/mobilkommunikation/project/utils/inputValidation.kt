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

fun validateServerPortNumber(portNumber: String): PortValidationResult {
    // elvis statement: when transforming to int returns null, return valid (later getAvailablePort())
    val port = portNumber.toIntOrNull() ?: return PortValidationResult.Valid
    if (port !in 1024..65535) {
        return PortValidationResult.InvalidRange
    }
    return try {
        val socket = ServerSocket(port)
        socket.close()
        PortValidationResult.Valid
    } catch (e: Exception) {
        PortValidationResult.Blocked
    }
}
