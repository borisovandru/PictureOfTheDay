package geekbarains.material.model.repository

import retrofit2.Callback
import geekbarains.material.model.retrofit.APODServerResponseData
import geekbarains.material.model.retrofit.mars.MarsServerResponseData

interface NasaAPI {

    fun getPictureOfDayRetroFit(
        itemDate: String?,
        callback: Callback<APODServerResponseData>,
    )

    fun getEPIC(
        itemDate: String,
        callback: Callback<geekbarains.material.model.retrofit.epic.EPICServerResponseData>,
    )

    fun getPhotoFromMars(
        rover: String,
        earthDate: String,
        camera: String,
        callback: Callback<MarsServerResponseData>,
    )
}