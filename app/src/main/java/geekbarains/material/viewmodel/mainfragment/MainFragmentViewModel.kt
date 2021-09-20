package geekbarains.material.viewmodel.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbarains.material.BuildConfig
import geekbarains.material.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import geekbarains.material.model.retrofit.APODServerResponseData

class MainFragmentViewModel(
    private val liveDataForViewToObserve: MutableLiveData<geekbarains.material.model.AppState> = MutableLiveData(),
    private val retrofitImpl: geekbarains.material.model.repository.NasaAPIImpl = geekbarains.material.model.repository.NasaAPIImpl(),
) :
    ViewModel() {

    fun getData(itemDate: String?): LiveData<geekbarains.material.model.AppState> {
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
                    geekbarains.material.model.AppState.Success(response.body()!!)
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {

                    val gv: geekbarains.material.PictureOfTheDay? = null
                    if (gv != null) {
                        gv.getApplication()
                        liveDataForViewToObserve.value =
                            geekbarains.material.model.AppState.Error(Throwable(gv.getApplication()
                                ?.getString(R.string.undefError)))
                    }
                } else {
                    liveDataForViewToObserve.value =
                        geekbarains.material.model.AppState.Error(Throwable(message))
                }
            }
        }

        override fun onFailure(call: Call<APODServerResponseData>, t: Throwable) {
            liveDataForViewToObserve.value = geekbarains.material.model.AppState.Error(t)
        }
    }

    private fun sendServerRequest(itemDate: String?) {
        liveDataForViewToObserve.value = geekbarains.material.model.AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            val gv: geekbarains.material.PictureOfTheDay? = null
            if (gv != null) {
                gv.getApplication()
                liveDataForViewToObserve.value =
                    geekbarains.material.model.AppState.Error(Throwable(gv.getApplication()
                        ?.getString(R.string.blankAPIKey)))
            }
        } else {
            retrofitImpl.getPictureOfDayRetroFit(itemDate, callBack)
        }
    }
}