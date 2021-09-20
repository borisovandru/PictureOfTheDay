package geekbarains.material.util.sharedpref

import android.content.Context
import android.content.SharedPreferences
import geekbarains.material.Constant.NAME_SHARED_PREFERENCE
import geekbarains.material.R

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val contextLoc = context

    // Чтение настроек
    fun loadSettings(): geekbarains.material.model.Settings {
        return geekbarains.material.model.Settings(
            sharedPreferences.getInt(contextLoc.getString(R.string.ThemeId), 0)
        )
    }

    // Сохранение настроек
    fun saveSettings(settings: geekbarains.material.model.Settings) {
        val editor = sharedPreferences.edit()
        editor.putInt(contextLoc.getString(R.string.ThemeId), settings.themeId)
        editor.apply()
    }
}