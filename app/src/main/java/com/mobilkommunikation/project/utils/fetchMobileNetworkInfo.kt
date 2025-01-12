package com.mobilkommunikation.project.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
fun fetchMobileNetworkInfo(
    context: Context
): List<String> {
    // Initialize return list
    val mobileNetworkInfoList = mutableListOf<String>()
    // Get permissions for fetching mobile network info
    val canAccessNetworkState = checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
    val canAccessFineLocation = checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

    if(canAccessNetworkState || canAccessFineLocation){
        // Initialize telephony manager
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        mobileNetworkInfoList.add("[Connection & Network Info]")
        val dataState = when (telephonyManager.dataState) {
            TelephonyManager.DATA_CONNECTED -> "Connected"
            TelephonyManager.DATA_DISCONNECTED -> "Disconnected"
            TelephonyManager.DATA_SUSPENDED -> "Suspended"
            else -> "Unknown"
        }
        mobileNetworkInfoList.add("Data State: $dataState")

        val phoneType = when (telephonyManager.phoneType) {
            TelephonyManager.PHONE_TYPE_NONE -> "None"
            TelephonyManager.PHONE_TYPE_GSM -> "GSM"
            TelephonyManager.PHONE_TYPE_CDMA -> "CDMA"
            TelephonyManager.PHONE_TYPE_SIP -> "SIP"
            else -> "Unknown"
        }
        mobileNetworkInfoList.add("Phone Type: $phoneType")

        val networkType = when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
            TelephonyManager.NETWORK_TYPE_NR -> "5G NR"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
            TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO_0"
            TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO_A"
            TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
            else -> "Unknown"
        }
        mobileNetworkInfoList.add("Network Type: $networkType")

        mobileNetworkInfoList.add("\n[Cell Info]")
        try {
            val cellLocation = telephonyManager.cellLocation
            if (cellLocation is GsmCellLocation) {
                val cellID = cellLocation.cid
                val lac = cellLocation.lac
                mobileNetworkInfoList.add("GSM Cell Location:")
                mobileNetworkInfoList.add("    - Cell ID: $cellID")
                mobileNetworkInfoList.add("    - Location Area Code: $lac")
            } else {
                mobileNetworkInfoList.add("Cell Location: not a GSM cell location.")
                mobileNetworkInfoList.add("    - $cellLocation")
            }
        } catch (e: Exception) {
            mobileNetworkInfoList.add("Error retrieving cell location: ${e.message}")
        }

        // TODO: http-Request for longitude and latitude

        mobileNetworkInfoList.add("\n[Operator Info]")
        val networkOperatorName = telephonyManager.networkOperatorName
        if (networkOperatorName.isEmpty()) {
            mobileNetworkInfoList.add("Network Operator Name: Not Available")
        } else {
            mobileNetworkInfoList.add("Network Operator Name: $networkOperatorName")
        }

        val simOperatorName = telephonyManager.simOperatorName
        if (simOperatorName.isEmpty()) {
            mobileNetworkInfoList.add("SIM Operator Name: Not Available")
        } else {
            mobileNetworkInfoList.add("SIM Operator Name: $simOperatorName")
        }

        val mccMnc = telephonyManager.networkOperator
        if (mccMnc.length >= 5) {
            val mcc = mccMnc.substring(0, 3)
            val mnc = mccMnc.substring(3)
            mobileNetworkInfoList.add("MCC: $mcc")
            mobileNetworkInfoList.add("MNC: $mnc")
        } else if (mccMnc.isEmpty()) {
            mobileNetworkInfoList.add("MCC/MNC: Not Available.")
        } else {
            mobileNetworkInfoList.add("MCC/MNC: $mccMnc")
        }

    } else {
        mobileNetworkInfoList.add("Permission Denied: ACCESS_NETWORK_STATE or ACCESS_FINE_LOCATION.")
    }

    return mobileNetworkInfoList
}
