package geekbarains.material.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import geekbarains.material.R
import geekbarains.material.util.sharedpref.SharedPref
import geekbarains.material.view.mainfragment.MainFragment

class MainActivity : AppCompatActivity() {

    private fun constToStyle(const: Int): Int {
        return when (const) {
            0 -> R.style.AppThemeMars
            1 -> R.style.AppThemeEarth
            2 -> R.style.AppThemeMoon
            else -> 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = SharedPref(this).loadSettings()
        setTheme(constToStyle(settings.themeId))
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
