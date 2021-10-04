package geekbarains.material.ui.viewmodel.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbarains.material.BuildConfig
import geekbarains.material.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import geekbarains.material.ui.model.retrofit.response.APODServerResponseData
import geekbarains.material.ui.PictureOfTheDay

class MainFragmentViewModel(
    private val liveDataForViewToObserve: MutableLiveData<geekbarains.material.ui.model.AppState> = MutableLiveData(),
    private val retrofitImpl: geekbarains.material.ui.model.repository.NasaAPIImpl = geekbarains.material.ui.model.repository.NasaAPIImpl(),
) :
    ViewModel() {

    fun getData(itemDate: String?): LiveData<geekbarains.material.ui.model.AppState> {
        sendServerRequest(itemDate)
        return liveDataForViewToObserve
    }

    private val callBack = object :
        Callback<APODServerResponseData> {
        override fun onResponse(
            call: Call<APODServerResponseData>,
            response: Response<APODServerResponseData>,
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataForViewToObserve.value =
                    geekbarains.material.ui.model.AppState.SuccessAPOD(response.body()!!)
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {

                    val gv: PictureOfTheDay? = null
                    if (gv != null) {
                        gv.getApplication()
                        liveDataForViewToObserve.value =
                            geekbarains.material.ui.model.AppState.Error(
                                Throwable(
                                    gv.getApplication()
                                        ?.getString(R.string.undefError)
                                )
                            )
                    }
                } else {
                    liveDataForViewToObserve.value =
                        geekbarains.material.ui.model.AppState.Error(Throwable(message))
                }
            }
        }

        override fun onFailure(call: Call<APODServerResponseData>, t: Throwable) {
            liveDataForViewToObserve.value = geekbarains.material.ui.model.AppState.Error(t)
        }
    }

    private fun sendServerRequest(itemDate: String?) {
        liveDataForViewToObserve.value = geekbarains.material.ui.model.AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            val gv: PictureOfTheDay? = null
            if (gv != null) {
                gv.getApplication()
                liveDataForViewToObserve.value =
                    geekbarains.material.ui.model.AppState.Error(
                        Throwable(
                            gv.getApplication()
                                ?.getString(R.string.blankAPIKey)
                        )
                    )
            }
        } else {
            retrofitImpl.getPictureOfDayRetroFit(itemDate, callBack)
        }
    }
}