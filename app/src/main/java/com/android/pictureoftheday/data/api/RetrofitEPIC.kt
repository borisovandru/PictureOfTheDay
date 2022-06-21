package com.android.pictureoftheday.data.api

import com.android.pictureoftheday.response.EPICServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitEPIC {

    @GET("EPIC/api/natural/date/{date}")
    fun getEPIC(
        @Path("date") queryType1: String,
        @Query("api_key") apiKey: String,
    ): Call<EPICServerResponseData>
}