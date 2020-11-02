package com.setiawan.mysubmission.ui.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.setiawan.mysubmission.ui.MainActivity
import com.setiawan.mysubmission.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler().postDelayed({
            val move = Intent(this, MainActivity::class.java)
            startActivity(move)
            finish()
        },3000)
    }
}
