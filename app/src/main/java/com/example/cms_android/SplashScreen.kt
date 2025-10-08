package com.example.cms_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val accessToken = pref.getString("accessToken", null)

        if (accessToken != null) {

            startActivity(Intent(this, ProfilePage::class.java))
        } else {

            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }
}
