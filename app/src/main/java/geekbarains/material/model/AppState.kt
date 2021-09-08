package geekbarains.material.model

import geekbarains.material.model.retrofit.APODServerResponseData

sealed class AppState {
    data class Success(val serverResponseData: APODServerResponseData) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}
