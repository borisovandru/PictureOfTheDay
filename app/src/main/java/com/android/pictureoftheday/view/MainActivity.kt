package com.android.pictureoftheday.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.android.pictureoftheday.R
import com.android.pictureoftheday.util.sharedpref.SharedPref

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private fun constToStyle(const: Int): Int {
        return when (const) {
            0 -> R.style.AppThemeMars
            1 -> R.style.AppThemeEarth
            2 -> R.style.AppThemeMoon
            else -> 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        val settings = SharedPref(this).loadSettings()
        setTheme(constToStyle(settings.themeId))
        setContentView(R.layout.main_activity)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setupWithNavController(navController)
    }
}
