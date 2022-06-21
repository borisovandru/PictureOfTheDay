package com.android.pictureoftheday.domain.repository

import retrofit2.Callback
import com.android.pictureoftheday.data.model.APODModel
import com.android.pictureoftheday.response.EPICServerResponseData
import com.android.pictureoftheday.response.MarsServerResponseData

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