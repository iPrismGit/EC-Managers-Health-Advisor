package com.iprism.ecmhealthadvisor.modals.addleads

data class UserDropDownsApiResponse(

    val message: String,
    val response: UserDropDownsResponse,
    val status: Boolean

)

data class UserDropDownsResponse(

    val blood_groups: List<BloodGroup>,
    val categories: List<Category>,
    val gender: List<Gender>

)

data class BloodGroup(

    val id: String,
    val name: String

)

data class Category(

    val id: String,
    val name: String

)

data class Gender(

    val id: String,
    val name: String

)