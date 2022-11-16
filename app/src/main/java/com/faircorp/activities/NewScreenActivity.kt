package com.faircorp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.faircorp.R


@SuppressLint("CustomSplashScreen")
class NewScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, BuildingsActivity::class.java))
            finish()
        }, 1500)

    }
}