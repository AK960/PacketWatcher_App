package com.mobilkommunikation.project.utils

import java.net.ServerSocket

fun getAvailablePort(): Int {
    ServerSocket(0).use { socket ->
        return socket.localPort
    }
}