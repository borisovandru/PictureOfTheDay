package geekbarains.material.model.repository

import retrofit2.Callback
import geekbarains.material.model.retrofit.response.APODServerResponseData
import geekbarains.material.model.retrofit.response.EPICServerResponseData
import geekbarains.material.model.retrofit.response.MarsServerResponseData

interface NasaAPI {

    fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODServerResponseData>,
    )

    fun getEPIC(
        itemDate: String,
        callback: Callback<EPICServerResponseData>,
    )

    fun getPhotoFromMars(
        rover: String,
        earthDate: String,
        camera: String,
        callback: Callback<MarsServerResponseData>,
    )
}