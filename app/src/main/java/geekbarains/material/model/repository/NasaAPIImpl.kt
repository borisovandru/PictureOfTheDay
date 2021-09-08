package geekbarains.material.model.repository

import com.google.gson.GsonBuilder
import geekbarains.material.BuildConfig
import geekbarains.material.Constant.BASE_API_URL
import geekbarains.material.model.retrofit.RetroFitInterface
import geekbarains.material.model.retrofit.APODServerResponseData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    private val retroFitBuilder = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(createOkHttpClient(PODInterceptor()))
        .build().create(RetroFitInterface::class.java)

    override fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODServerResponseData>,
    ) {
        retroFitBuilder.getPictureOfItemDay(
            BuildConfig.NASA_API_KEY,
            itemDate
        )
            .enqueue(callback)
    }
}