package com.android.pictureoftheday.data

import com.android.pictureoftheday.data.model.APODModel
import com.android.pictureoftheday.response.EPICServerResponseData
import com.android.pictureoftheday.response.MarsServerResponseData

sealed class AppState {
    data class SuccessAPOD(val model: APODModel) : AppState()
    data class SuccessEPIC(val serverResponseData: EPICServerResponseData) : AppState()
    data class SuccessMars(val serverResponseData: MarsServerResponseData) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}
