package geekbarains.material

import android.app.Application

class PictureOfTheDay : Application() {

    private var appInstance: Application? = null

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    fun getApplication(): Application? {
        return appInstance
    }
}