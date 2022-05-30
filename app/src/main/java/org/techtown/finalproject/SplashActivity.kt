package org.techtown.finalproject

import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed(Runnable {
            val view = findViewById<View>(R.id.view)
            val option = ActivityOptions.makeSceneTransitionAnimation(
                this,view,"temp"

            )
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent,option.toBundle())
            finish()
        }, 2000)
    }
}