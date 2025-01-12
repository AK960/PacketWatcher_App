package com.mobilkommunikation.project.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.Hashtable

fun getCellGeolocation(
    cellID: Int,
    lac: Int,
    mcc: Int = 262,
    mnc: Int = 0
): Hashtable<String, Any>? {
    myLog(msg="Fetching Geolocation with values CellID: $cellID, LAC: $lac, MCC: $mcc, MNC: $mnc")
    val client = OkHttpClient()

    // OpenCellID apiUrl and apiKey
    val apiKey = "pk.d8c6cc207c23161e4c8cd2c5524b061e"
    val url = "https://api.opencellid.org/cell/get?key=$apiKey&mcc=$mcc&mnc=$mnc&lac=$lac&cellid=$cellID"

    // Build request
    val request = Request.Builder()
        .url(url)
        .build()

    // Send request
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            myLog(msg="Request failed: ${response.code}")
            return null
        } else {
            val response = response.body?.string()
            myLog(msg="API Response: $response")

            // Transform response
            val jsonResponse = response?.let { JSONObject(it) }
            val responseHashtable = Hashtable<String, Any>()

            jsonResponse?.keys()?.forEach { key ->
                responseHashtable[key] = jsonResponse.get(key)
            }

            return responseHashtable
        }
    }
}