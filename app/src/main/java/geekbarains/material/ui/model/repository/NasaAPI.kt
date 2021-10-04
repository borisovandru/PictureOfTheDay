package geekbarains.material.ui.model.repository

import retrofit2.Callback
import geekbarains.material.ui.model.retrofit.response.APODServerResponseData
import geekbarains.material.ui.model.retrofit.response.EPICServerResponseData
import geekbarains.material.ui.model.retrofit.response.MarsServerResponseData

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