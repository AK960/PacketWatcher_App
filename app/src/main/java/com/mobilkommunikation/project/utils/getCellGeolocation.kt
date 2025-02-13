package com.mobilkommunikation.project.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.util.Hashtable
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun getCellGeolocation(
    cellID: Int,
    lac: Int,
    mcc: Int = 262,
    mnc: Int = 3
): Hashtable<String, Any>? = withContext(Dispatchers.IO) {
    myLog(tag= "myGET", msg = "Fetching Geolocation with values CellID: $cellID, LAC: $lac, MCC: $mcc, MNC: $mnc")
    val client = OkHttpClient()

    // OpenCellID API-URL und API-Key
    val apiKey = "pk.myApiKey"
    val url = "https://api.opencellid.org/cell/get?key=$apiKey&mcc=$mcc&mnc=$mnc&lac=$lac&cellid=$cellID&format=json"
    myLog(tag= "myGET", msg="URL: $url")

    // Request erstellen
    val request = Request.Builder()
        .url(url)
        .build()
    myLog(tag = "myGET", msg = "Request built: $request")

    try {
        val response = client.makeAsyncRequest(request)
        if (!response.isSuccessful) {
            myLog(type = "error", tag = "myGET", msg = "Request failed: ${response.code}")
            return@withContext null
        }

        val responseBody = response.body?.string()
        myLog(tag= "myGET", msg = "API Response: $responseBody")

        // JSON-Antwort verarbeiten
        val jsonResponse = responseBody?.let { JSONObject(it) }
        val responseHashtable = Hashtable<String, Any>()

        jsonResponse?.keys()?.forEach { key ->
            responseHashtable[key] = jsonResponse.get(key)
        }
        myLog(tag= "myGET", msg="JSON-response: $jsonResponse")

        return@withContext responseHashtable
    } catch (e: Exception) {
        myLog(tag= "myGET", msg = "Request failed with exception: ${e.message}")
        return@withContext null
    }
}

suspend fun OkHttpClient.makeAsyncRequest(request: Request): Response =
    suspendCancellableCoroutine { continuation ->
        myLog(tag= "myGET", msg="Starting asynchRequest.")
        val call = newCall(request)
        call.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                if (continuation.isActive) {
                    myLog(type = "error", tag= "myGET", msg="onFailure: $e")
                    continuation.resumeWithException(e)
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (continuation.isActive) {
                    myLog(tag= "myGET", msg="onResponse: $response")
                    continuation.resume(response)
                }
            }
        })

        continuation.invokeOnCancellation {
            myLog(tag= "myGET", msg="invokeOnCancellation")
            call.cancel()
        }
    }
