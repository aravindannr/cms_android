package com.example.cms_android.model


data class CandidateProfileModal(
    val data: UserData?,
    val errorCode: String?,
    val message: String?,
    val path: String?,
    val success: Boolean?
)

data class UserData(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val countryCode: String?,
    val mobile: String?,
    val email: String?,
    val gender: String?,
    val dateOfBirth: String?,
    val maritalStatus: String?,
    val address: String?,
    val designation: String?,
    val department: String?,
    val currentCompany: String?,
    val joinDate: String?,
    val profilePic: String?,
    val orgName: String?,
    val orgId: String?,
    val skillAndLevels: List<SkillAndLevel>?,
    val latestExperiances: List<LatestExperiance>?,
    val profileProgress: ProfileProgress?,
    val departmentsAndRoles: List<DepartmentsAndRole>?,
    val generalCode: String?,
    val shiftCode: String?,
    val incidentCode: String?,
    val scIncident: String?,
    val sessionCode: String?,
    val orgLogoDdl: String?,
    val candidateDoc: String?,
    val employee: Boolean?,
    val active: Boolean?
)

data class SkillAndLevel(
    val id: String?,
    val skill: String?,
    val skillLevel: String?,
    val certificateUrl: String?,
    val active: Boolean?
)

data class LatestExperiance(
    val id: String?,
    val companyName: String?,
    val designation: String?,
    val workedFrom: String?,
    val workedTill: String?,
    val active: Boolean?
)

data class ProfileProgress(
    val profileDetails: String?,
    val screening: String?,
    val interview: String?,
    val selection: String?,
    val onBoarding: String?
)

data class DepartmentsAndRole(
    val id: String?,
    val department: String?,
    val description: String?,
    val active: Boolean?,
    val roles: List<UserRole>?
)

data class UserRole(
    val id: String?,
    val role: String?,
    val active: Boolean?,
    val description: String?
)