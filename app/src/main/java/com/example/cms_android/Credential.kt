package com.example.cms_android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Credential (
    val username: String,
    val password: String
): Parcelable