package com.example.cms_android.network

enum class Environment {
    DEV, TEST, UAT, PROD
}

object AppConfig {
    val currentEnvironment: Environment = Environment.DEV
    val baseUrl8082: String
        get() = when (currentEnvironment) {
            Environment.DEV -> "https://cmsdev.primacycare.au/v1/api/user-onboarding"
            Environment.TEST -> "https://cmstest.primacycare.au/v1/api/user-onboarding"
            Environment.UAT -> "https://cmsuat.primacycare.au/v1/api/user-onboarding"
            Environment.PROD -> "https://prod.primacycare.au/v1/api/user-onboarding"
        }
    val baseUtl8085: String
        get() = when (currentEnvironment) {
            Environment.DEV -> "https://cmsdev.primacycare.au/v1/api/file-manipulation"
            Environment.TEST -> "https://cmstest.primacycare.au/v1/api/file-manipulation"
            Environment.UAT -> "https://cmsuat.primacycare.au/v1/api/file-manipulation"
            Environment.PROD -> "https://prod.primacycare.au/v1/api/file-manipulation"
        }
    val baseUrl8086: String
        get() = when (currentEnvironment) {
            Environment.DEV -> "https://cmsdev.primacycare.au/v1/api/allocation-management"
            Environment.TEST -> "https://cmstest.primacycare.au/v1/api/allocation-management"
            Environment.UAT -> "https://cmsuat.primacycare.au/v1/api/allocation-management"
            Environment.PROD -> "https://prod.primacycare.au/v1/api/allocation-management"
        }

    val baseUrl8083: String
        get() = when (currentEnvironment) {
            Environment.DEV -> "https://cmsdev.primacycare.au/v1/api/master-config"
            Environment.TEST -> "https://cmstest.primacycare.au/v1/api/master-config"
            Environment.UAT -> "https://cmsuat.primacycare.au/v1/api/master-config"
            Environment.PROD -> "https://prod.primacycare.au/v1/api/master-config"
        }
}