package com.mobilkommunikation.project.utils

fun isValidIpAddress(ipAddress: String): Boolean {
    val ipPattern = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    return ipPattern.matches(ipAddress)
}

fun isValidPortNumber(portNumber: String): Boolean {
    val port = portNumber.toIntOrNull()
    return port != null && port in 1..65535
}