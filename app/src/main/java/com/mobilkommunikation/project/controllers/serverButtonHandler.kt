package com.mobilkommunikation.project.controllers

fun handleStartServerInteraction(
    ipAddress: String,
    protocolSelected: String
) {
    if (protocolSelected == "TCP") {
        myLog(msg = "serverButtonHandler: $protocolSelected-Protocol selected")
        startServerSocket(ipAddress = ipAddress, protocolSelected = protocolSelected)
    } else
        myLog(msg = "serverButtonHandler: $protocolSelected-Protocol selected")
}