package geekbarains.material.ui.model

import geekbarains.material.ui.model.retrofit.response.APODServerResponseData
import geekbarains.material.ui.model.retrofit.response.EPICServerResponseData
import geekbarains.material.ui.model.retrofit.response.MarsServerResponseData

sealed class AppState {
    data class SuccessAPOD(val serverResponseData: APODServerResponseData) : AppState()
    data class SuccessEPIC(val serverResponseData: EPICServerResponseData) : AppState()
    data class SuccessMars(val serverResponseData: MarsServerResponseData) : AppState()
    data class Error(val error: Throwable) : geekbarains.material.ui.model.AppState()
    data class Loading(val progress: Int?) : geekbarains.material.ui.model.AppState()
}
