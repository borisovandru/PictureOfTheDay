package geekbarains.material.viewmodel.mainfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbarains.material.BuildConfig
import geekbarains.material.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import geekbarains.material.data.model.APODModel
import geekbarains.material.PictureOfTheDay
import geekbarains.material.data.AppState

class MainFragmentViewModel(
    private val liveDataForViewToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: geekbarains.material.domain.repository.NasaAPIImpl = geekbarains.material.domain.repository.NasaAPIImpl(),
) :
    ViewModel() {

    fun getData(itemDate: String?): LiveData<AppState> {
        sendServerRequest(itemDate)
        return liveDataForViewToObserve
    }

    private val callBack = object :
        Callback<APODModel> {
        override fun onResponse(
            call: Call<APODModel>,
            response: Response<APODModel>,
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataForViewToObserve.value =
                    AppState.SuccessAPOD(response.body()!!)
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {

                    val gv: PictureOfTheDay? = null
                    if (gv != null) {
                        gv.getApplication()
                        liveDataForViewToObserve.value =
                            AppState.Error(
                                Throwable(
                                    gv.getApplication()
                                        ?.getString(R.string.undefError)
                                )
                            )
                    }
                } else {
                    liveDataForViewToObserve.value =
                        AppState.Error(Throwable(message))
                }
            }
        }

        override fun onFailure(call: Call<APODModel>, t: Throwable) {
            liveDataForViewToObserve.value = AppState.Error(t)
        }
    }

    private fun sendServerRequest(itemDate: String?) {
        liveDataForViewToObserve.value = AppState.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            val gv: PictureOfTheDay? = null
            if (gv != null) {
                gv.getApplication()
                liveDataForViewToObserve.value =
                    AppState.Error(
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