package geekbarains.material.model.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import geekbarains.material.model.retrofit.epic.EPICServerResponseData

interface RetroFitEPICInterface {

    @GET("EPIC/api/natural/date/{date}")
    fun getEPIC(
        @Path("date") queryType1: String,
        @Query("api_key") apiKey: String,
    ): Call<EPICServerResponseData>
}