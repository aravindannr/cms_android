package com.example.cms_android

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cms_android.model.CandidateProfileModal
import com.example.cms_android.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilepage)

        // Get stored tokens and ID
        val pref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val accessToken = pref.getString("accessToken", null)
        val id = pref.getString("id", null)

        Log.d("ProfilePage", "AccessToken: ${accessToken?.take(20)}...")
        Log.d("ProfilePage", "ID: $id")

        if (!accessToken.isNullOrEmpty() && !id.isNullOrEmpty()) {
            fetchProfileDetails(id, accessToken, this)
        } else {
            Log.e(
                "ProfilePage",
                "Missing token or ID - Token: ${accessToken != null}, ID: ${id != null}"
            )
        }
    }

    private fun fetchProfileDetails(id: String, token: String, context: Context) {
        // Run API call in background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val profile: CandidateProfileModal? =
                    ApiService.getProfileDetails(id, token, context)

                withContext(Dispatchers.Main) {
                    if (profile != null) {
                        Log.d("ProfilePage", "Profile received - Success: ${profile.success}")
                        Log.d("ProfilePage", "Profile message: ${profile.message}")

                        if (profile.success == true && profile.data != null) {
                            val user = profile.data
                            Log.d(
                                "ProfileData",
                                """
                                Name: ${user.firstName} ${user.lastName}
                                Mobile: ${user.mobile}
                                Email: ${user.email}
                                Gender: ${user.gender}
                                DOB: ${user.dateOfBirth}
                                Marital: ${user.maritalStatus}
                                Address: ${user.address}
                                """.trimIndent()
                            )

                            // Update UI with profile data
                            updateUI(user)
                        } else {
                            Log.e("ProfilePage", "Profile success=false or data is null")
                            Log.e("ProfilePage", "Error: ${profile.errorCode} - ${profile.message}")
                        }
                    } else {
                        Log.e("ProfilePage", "Profile object is null - API call failed")
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfilePage", "Exception in fetchProfileDetails: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    // Show error to user
                }
            }
        }
    }

    private fun updateUI(user: com.example.cms_android.model.UserData) {
        try {
            findViewById<TextView>(R.id.tv_firstname)?.text = user.firstName ?: ""
            findViewById<TextView>(R.id.tv_lastname)?.text = user.lastName ?: ""
            findViewById<TextView>(R.id.tv_mobile)?.text =
                user.mobile ?: ""
            findViewById<TextView>(R.id.tv_email)?.text = user.email ?: ""
            findViewById<TextView>(R.id.tv_gender)?.text = user.gender?.uppercase() ?: ""
            findViewById<TextView>(R.id.tv_dob)?.text = user.dateOfBirth ?: ""
            findViewById<TextView>(R.id.tv_marital_status)?.text =
                user.maritalStatus?.uppercase() ?: ""
            findViewById<TextView>(R.id.tv_address)?.text = user.address ?: ""

            Log.d("ProfilePage", "UI updated successfully")
        } catch (e: Exception) {
            Log.e("ProfilePage", "Error updating UI: ${e.message}", e)
        }
    }
}