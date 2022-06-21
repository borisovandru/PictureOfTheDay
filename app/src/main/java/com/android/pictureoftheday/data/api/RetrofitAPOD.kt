package com.android.pictureoftheday.data.api

import com.android.pictureoftheday.data.model.APODModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPOD {

    @GET("planetary/apod")
    fun getPictureOfItemDay(
        @Query("api_key") apiKey: String,
        @Query("date") itemDate: String?,
        @Query("thumbs") thumbs: Boolean = true
    ): Call<APODModel>
}