package com.iprism.ecmhealthadvisor.modals.authentication

data class ProfileApiResponse(

    val message: String,
    val response: ProfileResponse,
    val status: Boolean

)

data class ProfileResponse(

    val profile: Profile

)

data class Profile(

    val blood_group: String,
    val blood_group_name: String,
    val delete_status: String,
    val email: String,
    val gender: String,
    val gender_name: String,
    val dob: String,
    val id: String,
    val image: String,
    val mobile: String,
    val name: String,
    val status: String,
    val user_id: String

)