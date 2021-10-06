package geekbarains.material.ui.view

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import geekbarains.material.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private var handler = Handler()
    private lateinit var rocketAnimation: AnimationDrawable

    override fun onStart() {
        super.onStart()
        rocketAnimation.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashScreen = image_view.apply {
            setBackgroundResource(R.drawable.animation_splash_screen)
            rocketAnimation = background as AnimationDrawable
        }
        splashScreen.setOnClickListener { rocketAnimation.start() }


        /*image_view.animate().rotationBy(750f)
            .setInterpolator(LinearInterpolator()).duration = 10000*/

        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}