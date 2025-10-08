package com.example.cms_android.network

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.cms_android.model.CandidateProfileModal
import com.example.cms_android.model.LoginOtpResponse
import com.example.cms_android.model.LoginResponse
import com.example.cms_android.model.RefreshTokenModal
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


object ApiService {
    private val client = OkHttpClient()
    private val gson = Gson()


    private fun sendRequest(
        requestBuilder: Request.Builder,
        token: String,
        context: Context
    ): Response? {
        return try {
            // Add authorization header
            val request = requestBuilder
                .addHeader("Authorization", "Bearer $token")
                .build()

            val response = client.newCall(request).execute()

            Log.d("ApiService", "Response Code: ${response.code}")

            // Handle 401 Unauthorized - Token expired
            if (response.code == 401) {
                Log.w("ApiService", "Token expired (401), attempting refresh...")
                response.close()
                val prefs = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)

                val email = prefs?.getString("username", null)
                val refreshToken = prefs?.getString("refreshToken", null)

                if (!email.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                    // Try to refresh token
                    val refreshResponse = refreshToken(email, refreshToken)

                    if (refreshResponse?.success == true &&
                        !refreshResponse.data?.accessToken.isNullOrEmpty()
                    ) {

                        val newAccessToken = refreshResponse.data.accessToken

                        // Update SharedPreferences with new token
                        prefs.edit()?.apply {
                            putString("accessToken", newAccessToken)
                            refreshResponse.data.refreshToken?.let {
                                putString("refreshToken", it)
                            }
                            apply()
                        }

                        Log.d("ApiService", "Token refreshed successfully, retrying request")

                        // Retry original request with new token
                        val retryRequest = requestBuilder
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer $newAccessToken")
                            .build()

                        return client.newCall(retryRequest).execute()
                    } else {
                        Log.e("ApiService", "Token refresh failed")
                        return null
                    }
                } else {
                    Log.e("ApiService", "Missing email or refresh token")
                    return null
                }
            }

            response
        } catch (e: Exception) {
            Log.e("ApiService", "Request error: ${e.message}", e)
            null
        }
    }

    fun refreshToken(userName: String, refreshToken: String): RefreshTokenModal? {
        return try {
            val json = """
                {
                    "userName":"$userName",
                    "refreshToken":"$refreshToken"
                }
            """.trimIndent()

            val body = json.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("${AppConfig.baseUrl8082}/auth/refresh-token")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                Log.d("ApiService", "Refresh Token Response Code: ${response.code}")
                Log.d("ApiService", "Refresh Token Response: $responseBody")

                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    gson.fromJson(responseBody, RefreshTokenModal::class.java)
                } else {
                    Log.e("ApiService", "Token refresh failed with code: ${response.code}")
                    RefreshTokenModal(
                        data = null,
                        success = false,
                        message = "Failed to refresh token",
                        errorCode = response.code.toString(),
                        path = "${AppConfig.baseUrl8082}/auth/refresh-token"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Token refresh error: ${e.message}", e)
            RefreshTokenModal(
                data = null,
                success = false,
                message = "Exception: ${e.message}",
                errorCode = null,
                path = null
            )
        }
    }

    fun login(userName: String, password: String, localTimezone: String): LoginResponse? {
        return try {
            val json = """
                {
                    "userName":"$userName",
                    "password":"$password",
                    "currentTimezone":"$localTimezone"
                }
            """.trimIndent()

            val body = json.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("${AppConfig.baseUrl8082}/auth/authentication")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                Log.d("ApiService", "Response: $responseBody")

                if (responseBody != null) {
                    gson.fromJson(responseBody, LoginResponse::class.java)
                } else null
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Login error: ${e.message}")
            null
        }
    }

    fun loginOtpAuthorization(
        userName: String?,
        password: String?,
        otp: String,
        localTimezone: String
    ): LoginOtpResponse? {
        return try {
            val json = """
                {
                    "userName":"$userName",
                    "password":"$password",
                    "otp": "$otp",
                    "currentTimezone":"$localTimezone"
                }
                """.trimIndent()
            val body = json.toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("${AppConfig.baseUrl8082}/auth/authorization")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()
            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                Log.d("ApiService", "Response: $responseBody")
                if (responseBody != null) {
                    gson.fromJson(responseBody, LoginOtpResponse::class.java)
                } else null
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Login error: ${e.message}")
            null
        }
    }

    fun getProfileDetails(id: String, token: String, context: Context): CandidateProfileModal? {
        return try {
            val url = "${AppConfig.baseUrl8082}/canditate-profile/view?id=$id"
            Log.d("ApiService", "Profile URL: $url")
            Log.d("ApiService", "Token: Bearer $token")
            val request =
                Request.Builder().url(url).get().addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer $token")
            val response = sendRequest(request, token, context)
            response?.use {
                val responseBody = it.body?.string()
                Log.d("ApiService", "Profile Response Code: ${it.code}")
                Log.d("ApiService", "Profile Response Body: $responseBody")
                if (it.isSuccessful && !responseBody.isNullOrEmpty()) {
                    try {
                        gson.fromJson(responseBody, CandidateProfileModal::class.java)
                    } catch (e: Exception) {
                        Log.e("ApiService", "JSON parsing error: ${e.message}", e)
                        null
                    }
                } else {
                    Log.e("ApiService", "Profile failed - Code: ${it.code}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ApiService", "getProfileDetails error: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }

    fun getRequest(url: String, token: String, context: Context): String? {
        return try {
            val requestBuilder = Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")

            val response = sendRequest(requestBuilder, token, context)

            response?.use {
                if (it.isSuccessful) {
                    it.body?.string()
                } else {
                    Log.e("ApiService", "GET request failed - Code: ${it.code}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ApiService", "GET request error: ${e.message}", e)
            null
        }
    }

    // ============================================
    // Generic POST Request with Token Refresh
    // ============================================
    fun postRequest(url: String, token: String, jsonBody: String, context: Context): String? {
        return try {
            val body = jsonBody.toRequestBody("application/json".toMediaType())

            val requestBuilder = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")

            val response = sendRequest(requestBuilder, token, context)

            response?.use {
                if (it.isSuccessful) {
                    it.body?.string()
                } else {
                    Log.e("ApiService", "POST request failed - Code: ${it.code}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ApiService", "POST request error: ${e.message}", e)
            null
        }
    }
}