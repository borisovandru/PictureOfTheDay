package com.android.pictureoftheday.domain.repository

import com.google.gson.GsonBuilder
import com.android.pictureoftheday.BuildConfig
import com.android.pictureoftheday.util.Constant.BASE_API_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.android.pictureoftheday.data.model.APODModel
import com.android.pictureoftheday.data.api.RetrofitAPOD
import com.android.pictureoftheday.data.api.RetrofitEPIC
import com.android.pictureoftheday.data.api.RetrofitMars
import com.android.pictureoftheday.response.EPICServerResponseData
import com.android.pictureoftheday.response.MarsServerResponseData
import java.io.IOException

class NasaAPIImpl : NasaAPI {

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class PODInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }

    private val retroFitBuilderAPOD = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(PODInterceptor()))
        .build().create(RetrofitAPOD::class.java)

    private val retroFitBuilderEPIC = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(PODInterceptor()))
        .build().create(RetrofitEPIC::class.java)

    private val retroFitBuilderMars = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(PODInterceptor()))
        .build().create(RetrofitMars::class.java)

    override fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODModel>,
    ) {
        retroFitBuilderAPOD.getPictureOfItemDay(
            BuildConfig.NASA_API_KEY,
            itemDate
        )
            .enqueue(callback)
    }

    override fun getEPIC(
        itemDate: String,
        callback: Callback<EPICServerResponseData>
    ) {
        retroFitBuilderEPIC.getEPIC(
            itemDate,
            BuildConfig.NASA_API_KEY
        )
            .enqueue(callback)
    }

    override fun getPhotoFromMars(
        rover: String,
        earthDate: String,
        camera: String,
        callback: Callback<MarsServerResponseData>,
    ) {
        retroFitBuilderMars.getMarsPhoto(
            rover,
            earthDate,
            camera,
            BuildConfig.NASA_API_KEY
        )
            .enqueue(callback)
    }
}