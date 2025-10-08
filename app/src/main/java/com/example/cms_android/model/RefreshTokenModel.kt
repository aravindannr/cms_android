package com.example.cms_android.model

data class RefreshTokenModal(
    val data: RefreshTokenData?,
    val errorCode: String?,
    val message: String?,
    val path: String?,
    val success: Boolean?
)

data class RefreshTokenData(
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
    val roleUiAccess: List<Any>?,
    val departments: List<TokenDepartment>?,
    val orgId: String?,
    val organisation: TokenOrganisation?
)

data class TokenDepartment(
    val id: String?,
    val department: String?,
    val description: String?,
    val active: Boolean?,
    val roles: List<TokenRole>?
)

data class TokenRole(
    val id: String?,
    val role: String?,
    val active: Boolean?,
    val description: String?
)

data class TokenOrganisation(
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