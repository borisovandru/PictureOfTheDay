package geekbarains.material

import android.app.Application
import geekbarains.material.model.note.Note

class PictureOfTheDay : Application() {

    private var appInstance: Application? = null
    lateinit var notes: List<Note>

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    fun getApplication(): Application? {
        return appInstance
    }
}