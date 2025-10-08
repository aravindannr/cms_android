package com.example.cms_android.model

data class LoginResponse(
    val data: Boolean?,
    val errorCode: Int?,
    val message: String?,
    val path: String?,
    val success: Boolean?
)
