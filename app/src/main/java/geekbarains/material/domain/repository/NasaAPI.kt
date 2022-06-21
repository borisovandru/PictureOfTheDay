package geekbarains.material.domain.repository

import retrofit2.Callback
import geekbarains.material.data.model.APODModel
import geekbarains.material.model.retrofit.response.EPICServerResponseData
import geekbarains.material.model.retrofit.response.MarsServerResponseData

interface NasaAPI {

    fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODModel>,
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