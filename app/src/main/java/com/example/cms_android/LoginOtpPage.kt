package com.example.cms_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cms_android.network.ApiService
import com.example.cms_android.utils.TimezoneUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginOtpPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        val otpInput = findViewById<EditText>(R.id.otpInput)
        val loginOtpButton = findViewById<Button>(R.id.loginOtpButton)
        val timezone = TimezoneUtils.detectAndPrintTimezoneInfo()

        val creds = intent.getParcelableExtra<Credential>("extra-creds")
        val username = creds?.username
        val password = creds?.password

        loginOtpButton.setOnClickListener {
            val username = username.toString()
            val password = password.toString()
            val otp = otpInput.text.toString()
            val timezone = timezone
            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiService.loginOtpAuthorization(username, password, otp, timezone)

                withContext(Dispatchers.Main) {
                    if (response != null && response.success == true) {

                        val pref = getSharedPreferences("MyPref", MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putString("accessToken", response.data?.accessToken)
                        editor.putString("refreshToken", response.data?.refreshToken)
                        editor.putString("id", response.data?.id)
                        editor.putString("username", username)
                        editor.apply()
                        Toast.makeText(this@LoginOtpPage, "Login Success", Toast.LENGTH_SHORT)
                            .show()
                        val indent = Intent(this@LoginOtpPage, ProfilePage::class.java)
                        startActivity(indent)
                    } else {
                        Toast.makeText(
                            this@LoginOtpPage,
                            "Login Failed: ${response?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("LoginActivity", "Error: ${response?.message}")
                    }
                }
            }
        }
        Log.d("LoginOtpPage", "Username: $username, Password: $password")
    }
}