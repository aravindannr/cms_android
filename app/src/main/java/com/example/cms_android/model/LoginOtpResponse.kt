package com.example.cms_android.model

data class LoginOtpResponse(
    val data: LoginData?,
    val errorCode: Int?,
    val message: String?,
    val path: String?,
    val success: Boolean?
)

data class LoginData(
    val accessToken: String?,
    val idToken: String?,
    val refreshToken: String?,
    val tokenType: String?,
    val expiresIn: Int?,
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val mobile: String?,
    val userType: String?,
    val participantRoleName: String?,
    val departments: List<Department>?,
    val orgId: String?,
    val organisation: Organisation?
)

data class Department(
    val id: String?,
    val department: String?,
    val description: String?,
    val active: Boolean?,
    val roles: List<Role>?
)

data class Role(
    val id: String?,
    val role: String?,
    val active: Boolean?,
    val description: String?
)

data class Organisation(
    val id: String?,
    val name: String?,
    val generalCode: String?,
    val shiftCode: String?,
    val incidentCode: String?,
    val scIncident: String?,
    val sessionCode: String?,
    val logoDdl: String?,
    val active: Boolean?,
    val hasSubCategories: Boolean?
)
