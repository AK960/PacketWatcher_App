package com.mobilkommunikation.project.utils

import android.Manifest
import android.content.Context
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

@OptIn(DelicateCoroutinesApi::class)
suspend fun retrofitGetCellLocation (
    context: Context
): List<String> = withContext(Dispatchers.IO) {
    // Initialize return List
    val geolocationInfoList = mutableListOf<String>()
    // Get permissions for fetching data
    val canAccessNetworkState = checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
    val canAccessFineLocation = checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

    if (canAccessNetworkState || canAccessFineLocation) {
        val response = try {

        } catch (e: HttpException) {
            geolocationInfoList.add("")
        } catch (e: IOException) {
        }







    }

    return@withContext geolocationInfoList
}