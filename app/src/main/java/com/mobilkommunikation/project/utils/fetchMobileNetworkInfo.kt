package com.mobilkommunikation.project.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.telephony.CellIdentityCdma
import android.telephony.CellIdentityGsm
import android.telephony.CellIdentityLte
import android.telephony.CellIdentityWcdma
import android.telephony.CellInfo
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.P)
suspend fun fetchMobileNetworkInfo(
    context: Context
): List<String> = withContext(Dispatchers.IO) {
    // Initialize return list
    val mobileNetworkInfoList = mutableListOf<String>()
    // Get permissions for fetching mobile network info
    val canAccessNetworkState = checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
    val canAccessFineLocation = checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

    if (canAccessNetworkState || canAccessFineLocation) {
        // Initialize telephony manager
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var cid: Int? = null
        var lac: Int? = null
        var mcc: Int? = null
        var mnc: Int? = null

        // Operator Infos
        mobileNetworkInfoList.add("\n[Operator Info]")
        val networkOperatorName = telephonyManager.networkOperatorName
        mobileNetworkInfoList.add(
            if (networkOperatorName.isEmpty()) "Network Operator Name: Not Available"
            else "Network Operator Name: $networkOperatorName"
        )

        val simOperatorName = telephonyManager.simOperatorName
        mobileNetworkInfoList.add(
            if (simOperatorName.isEmpty()) "SIM Operator Name: Not Available"
            else "SIM Operator Name: $simOperatorName"
        )

        val mccMnc = telephonyManager.networkOperator
        if (mccMnc.length >= 5) {
            mcc = mccMnc.substring(0, 3).toInt()
            mnc = mccMnc.substring(3).toInt()
            mobileNetworkInfoList.add("MCC: $mcc") // default MCC Germany: 262
            mobileNetworkInfoList.add("MNC: $mnc") // default MNC blau.de: 03 -> part of O2
        } else if (mccMnc.isEmpty()) {
            mobileNetworkInfoList.add("MCC/MNC: Not Available.")
        } else {
            mobileNetworkInfoList.add("MCC/MNC: $mccMnc")
        }

        // Data State and Phone Type
        mobileNetworkInfoList.add("\n[Connection & Network Info]")
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

        val networkType = when (telephonyManager.dataNetworkType) {
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

        // Cell Info and Geolocation (deprecated)
        mobileNetworkInfoList.add("\n[Cell Info 1]")
        val cellLocation = telephonyManager.cellLocation
        if (cellLocation is GsmCellLocation) {
            cid = cellLocation.cid
            lac = cellLocation.lac
            mobileNetworkInfoList.add("GSM Cell Location:")
            mobileNetworkInfoList.add("    - Cell ID: $cid")
            mobileNetworkInfoList.add("    - Location Area Code: $lac")
        } else {
            mobileNetworkInfoList.add("Cell Location: not a GSM cell location.")
            mobileNetworkInfoList.add("    - $cellLocation")
        }

        // New method for fetching geolocation (alternative to deprecated getCellLocation)
        mobileNetworkInfoList.add("\n[Cell Info 2]")
        val cellInfoList: List<CellInfo> = telephonyManager.allCellInfo
        cellInfoList.forEach { cellInfo ->
            when(cellInfo) {
                is CellInfoCdma -> {
                    val cellIdentity: CellIdentityCdma = cellInfo.cellIdentity
                    val baseStation = cellIdentity.basestationId
                    val lat = cellIdentity.latitude
                    val long = cellIdentity.longitude
                    val nId = cellIdentity.networkId
                    val sysId = cellIdentity.systemId
                    mobileNetworkInfoList.add("CDMA Cell Location:")
                    mobileNetworkInfoList.add("    - Base Station ID: $baseStation")
                    mobileNetworkInfoList.add("    - Latitude: $lat")
                    mobileNetworkInfoList.add("    - Longitude: $long")
                    mobileNetworkInfoList.add("    - Network ID: $nId")
                    mobileNetworkInfoList.add("    - System ID: $sysId")
                }
                is CellInfoGsm -> {
                    val cellIdentity: CellIdentityGsm = cellInfo.cellIdentity
                    cid = cellIdentity.cid
                    lac = cellIdentity.lac
                    mcc = cellIdentity.mccString?.toInt()
                    mnc = cellIdentity.mncString?.toInt()
                    mobileNetworkInfoList.add("GSM Cell Location:")
                    mobileNetworkInfoList.add("    - Cell ID: $cid")
                    mobileNetworkInfoList.add("    - Location Area Code: $lac")
                    mobileNetworkInfoList.add("    - MCC: $mcc") // default MCC Germany: 262
                    mobileNetworkInfoList.add("    - MNC: $mnc") // default MNC blau.de: 3 -> part of O2
                }
                is CellInfoLte -> {
                    val cellIdentity: CellIdentityLte = cellInfo.cellIdentity
                    cid = cellIdentity.ci
                    lac = cellIdentity.tac
                    mcc = cellIdentity.mccString?.toInt()
                    mnc = cellIdentity.mncString?.toInt()
                    if (cid != 0 || lac != 0 || mcc != null || mnc != null) {
                        mobileNetworkInfoList.add("LTE Cell Location:")
                        mobileNetworkInfoList.add("    - Cell ID: $cid")
                        mobileNetworkInfoList.add("    - LAC: $lac")
                        mobileNetworkInfoList.add("    - MCC: $mcc")
                        mobileNetworkInfoList.add("    - MNC: $mnc")
                    }
                }
                is CellInfoWcdma -> {
                    val cellIdentity: CellIdentityWcdma = cellInfo.cellIdentity
                    cid = cellIdentity.cid
                    lac = cellIdentity.lac
                    mcc = cellIdentity.mccString?.toInt()
                    mnc = cellIdentity.mncString?.toInt()
                    mobileNetworkInfoList.add("WCDMA Cell Location:")
                    mobileNetworkInfoList.add("    - Cell ID: $cid")
                    mobileNetworkInfoList.add("    - LAC: $lac")
                    mobileNetworkInfoList.add("    - MCC: $mcc")
                    mobileNetworkInfoList.add("    - MNC: $mnc")
                }
                else -> {
                    mobileNetworkInfoList.add("Cell Location: not a CDMA, GSM, LTE or WCDMA cell location.")
                    mobileNetworkInfoList.add("    - $cellInfo")
                }
            }
        }

        // Geolocation
        mobileNetworkInfoList.add("\n[GeoLocation]")
        if (cid != null && lac != null && mcc != null && mnc != null) {
            myLog(tag= "myGET", msg = "Fetching Geolocation with values CellID: $cid, LAC: $lac, MCC: $mcc, MNC: $mnc")

            // Asynchronous call to fetch geolocation
            val geoLocation = withContext(Dispatchers.IO) {
                getCellGeolocation(cid!!, lac!!, mcc!!, mnc!!)
            }
            myLog(tag= "myGET", msg="geoLocation: $geoLocation")

            if (geoLocation != null) {
                geoLocation.forEach { key, value ->
                    mobileNetworkInfoList.add("$key: $value\n")
                }
            } else {
                mobileNetworkInfoList.add("Geolocation: Returned Null")
            }
        } else {
            mobileNetworkInfoList.add("Geolocation: Not Available. Parameters: CID: $cid, LAC: $lac, MCC: $mcc, MNC: $mnc")
        }
    } else {
        mobileNetworkInfoList.add("Permission Denied: ACCESS_NETWORK_STATE or ACCESS_FINE_LOCATION.")
    }

    return@withContext mobileNetworkInfoList
}
