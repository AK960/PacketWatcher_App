package com.mobilkommunikation.project.data

import android.telephony.CellLocation
import com.mobilkommunikation.project.model.OpenCellId
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/cell/get?key=$apiKey&mcc=$mcc&mnc=$mnc&lac=$lac&cellid=$cellID&format=json")
    suspend fun getCellLocation(): Response<OpenCellId>
}

