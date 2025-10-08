package com.example.cms_android.utils


import java.util.TimeZone
import java.util.Date

object TimezoneUtils {
    fun detectAndPrintTimezoneInfo(): String {
        val tz: TimeZone = TimeZone.getDefault()

        val deviceTimezone = tz.id // Example: "Asia/Kolkata", "Australia/Sydney"
        val offsetMillis = tz.rawOffset
        val offsetHours = offsetMillis / (1000 * 60 * 60)

        println("Device Timezone: $deviceTimezone")
        println("Offset (hours): $offsetHours")
        println("Current Time: ${Date()}")

        return deviceTimezone
    }
}
