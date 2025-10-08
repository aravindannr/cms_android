package com.example.cms_android

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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.userpasswordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val timezone = TimezoneUtils.detectAndPrintTimezoneInfo()
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString() // add another EditText for password
            val timezone = timezone // or detect dynamically

            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiService.login(username, password, timezone)

                withContext(Dispatchers.Main) {
                    if (response != null && response.success == true) {
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT)
                            .show()

                        // Navigate to DashboardActivity
                        val creds = Credential(username, password)
                        val intent = Intent(this@LoginActivity, LoginOtpPage::class.java).apply {
                            putExtra("extra-creds", creds)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Failed: ${response?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("LoginActivity", "Error: ${response?.message}")
                    }
                }
            }
        }
    }
}
