package geekbarains.material.model.repository

import geekbarains.material.model.retrofit.APODServerResponseData
import retrofit2.Callback

interface NasaAPI {
    fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODServerResponseData>,
    )
}