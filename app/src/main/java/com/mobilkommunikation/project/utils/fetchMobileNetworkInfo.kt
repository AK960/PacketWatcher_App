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
        var cid1: Int? = null
        var lac1: Int? = null
        var mcc1: Int? = null
        var mnc1: Int? = null
        var cid2: Int? = null
        var lac2: Int? = null
        var mcc2: Int? = null
        var mnc2: Int? = null

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
        mobileNetworkInfoList.add("\n[Cell Info 1: CellLocation(deprecated)/NetworkOperator]")
        val cellLocation = telephonyManager.cellLocation
        val mccMnc = telephonyManager.networkOperator
        if (cellLocation is GsmCellLocation && mccMnc.length >= 5) {
            cid1 = cellLocation.cid
            lac1 = cellLocation.lac
            mcc1 = mccMnc.substring(0, 3).toInt()
            mnc1 = mccMnc.substring(3).toInt()
            mobileNetworkInfoList.add("GSM Cell Location:")
            mobileNetworkInfoList.add("    - Cell ID: $cid1")
            mobileNetworkInfoList.add("    - LAC: $lac1")
            mobileNetworkInfoList.add("    - MCC: $mcc1") // default MCC Germany: 262
            mobileNetworkInfoList.add("    - MNC: $mnc1") // default MNC blau.de: 03 -> part of O2
        } else {
            mobileNetworkInfoList.add("Cell Location: not a GSM cell location or MCC/MNC not available.")
            mobileNetworkInfoList.add("    - $cellLocation")
            mobileNetworkInfoList.add("    - $mccMnc")
        }

        // New method for fetching geolocation (alternative to deprecated getCellLocation)
        mobileNetworkInfoList.add("\n[Cell Info 2: allCellInfo]")
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
                    if (baseStation != 0 || lat.toDouble() != 0.0 || long.toDouble() != 0.0 || nId != 0 || sysId != 0) {
                        mobileNetworkInfoList.add("CDMA Cell Location:")
                        mobileNetworkInfoList.add("    - Base Station ID: $baseStation")
                        mobileNetworkInfoList.add("    - Latitude: $lat")
                        mobileNetworkInfoList.add("    - Longitude: $long")
                        mobileNetworkInfoList.add("    - Network ID: $nId")
                        mobileNetworkInfoList.add("    - System ID: $sysId")
                    }
                }
                is CellInfoGsm -> {
                    val cellIdentity: CellIdentityGsm = cellInfo.cellIdentity
                    cid2 = cellIdentity.cid
                    lac2 = cellIdentity.lac
                    mcc2 = cellIdentity.mccString?.toInt()
                    mnc2 = cellIdentity.mncString?.toInt()
                    if (cid2 != 0 || lac2 != 0 || mcc2 != null || mnc2 != null) {
                        mobileNetworkInfoList.add("GSM Cell Location:")
                        mobileNetworkInfoList.add("    - Cell ID: $cid2")
                        mobileNetworkInfoList.add("    - Location Area Code: $lac2")
                        mobileNetworkInfoList.add("    - MCC: $mcc2") // default MCC Germany: 262
                        mobileNetworkInfoList.add("    - MNC: $mnc2") // default MNC blau.de: 3 -> part of O2
                    }
                }
                is CellInfoLte -> {
                    val cellIdentity: CellIdentityLte = cellInfo.cellIdentity
                    cid2 = cellIdentity.ci
                    lac2 = cellIdentity.tac
                    mcc2 = cellIdentity.mccString?.toInt()
                    mnc2 = cellIdentity.mncString?.toInt()
                    if (cid2 != 0 || lac2 != 0 || mcc2 != null || mnc2 != null) {
                        mobileNetworkInfoList.add("LTE Cell Location:")
                        mobileNetworkInfoList.add("    - Cell ID: $cid2")
                        mobileNetworkInfoList.add("    - LAC: $lac2")
                        mobileNetworkInfoList.add("    - MCC: $mcc2")
                        mobileNetworkInfoList.add("    - MNC: $mnc2")
                    }
                }
                is CellInfoWcdma -> {
                    val cellIdentity: CellIdentityWcdma = cellInfo.cellIdentity
                    cid2 = cellIdentity.cid
                    lac2 = cellIdentity.lac
                    mcc2 = cellIdentity.mccString?.toInt()
                    mnc2 = cellIdentity.mncString?.toInt()
                    if (cid2 != 0 || lac2 != 0 || mcc2 != null || mnc2 != null) {
                        mobileNetworkInfoList.add("WCDMA Cell Location:")
                        mobileNetworkInfoList.add("    - Cell ID: $cid2")
                        mobileNetworkInfoList.add("    - LAC: $lac2")
                        mobileNetworkInfoList.add("    - MCC: $mcc2")
                        mobileNetworkInfoList.add("    - MNC: $mnc2")
                    }
                }
                else -> {
                    mobileNetworkInfoList.add("Cell Location: not a CDMA, GSM, LTE or WCDMA cell location.")
                    mobileNetworkInfoList.add("    - $cellInfo")
                }
            }
        }

        // Geolocation
        mobileNetworkInfoList.add("\n[GeoLocation 1]")
        if (cid1 != null && lac1 != null && mcc1 != null && mnc1 != null) {
            myLog(tag= "myGET", msg = "Fetching Geolocation with values CellID: $cid1, LAC: $lac1, MCC: $mcc1, MNC: $mnc1")

            // Asynchronous call to fetch geolocation
            val geoLocation = withContext(Dispatchers.IO) {
                getCellGeolocation(cid1, lac1, mcc1, mnc1)
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
            mobileNetworkInfoList.add("Geolocation: Not Available. Parameters: CID: $cid1, LAC: $lac1, MCC: $mcc1, MNC: $mnc1")
        }

        mobileNetworkInfoList.add("\n[GeoLocation 2]")
        if (cid2 != null && lac2 != null && mcc2 != null && mnc2 != null) {
            myLog(tag= "myGET", msg = "Fetching Geolocation with values CellID: $cid1, LAC: $lac1, MCC: $mcc1, MNC: $mnc1")

            // Asynchronous call to fetch geolocation
            val geoLocation = withContext(Dispatchers.IO) {
                getCellGeolocation(cid2!!, lac2!!, mcc2!!, mnc2!!)
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
            mobileNetworkInfoList.add("Geolocation: Not Available. Parameters: CID: $cid1, LAC: $lac1, MCC: $mcc1, MNC: $mnc1")
        }

    } else {
        mobileNetworkInfoList.add("Permission Denied: ACCESS_NETWORK_STATE or ACCESS_FINE_LOCATION.")
    }

    return@withContext mobileNetworkInfoList
}
