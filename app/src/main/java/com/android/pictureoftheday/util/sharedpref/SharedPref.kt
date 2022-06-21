package com.android.pictureoftheday.util.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.android.pictureoftheday.util.Constant.NAME_SHARED_PREFERENCE
import com.android.pictureoftheday.R
import com.android.pictureoftheday.data.Settings

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    private val contextLoc = context

    // Чтение настроек
    fun loadSettings(): Settings {
        return Settings(
            sharedPreferences.getInt(contextLoc.getString(R.string.ThemeId), 0)
        )
    }

    // Сохранение настроек
    fun saveSettings(settings: Settings) {
        val editor = sharedPreferences.edit()
        editor.putInt(contextLoc.getString(R.string.ThemeId), settings.themeId)
        editor.apply()
    }
}