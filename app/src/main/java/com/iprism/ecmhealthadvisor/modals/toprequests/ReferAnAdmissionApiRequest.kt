package com.iprism.ecmhealthadvisor.modals.toprequests

data class ReferAnAdmissionApiRequest(

    val age: String,
    val auth_token: String,
    val image: String,
    val lat: String,
    val location: String,
    val lon: String,
    val main_data_id: String,
    val mobile: String,
    val name: String,
    val reason: String,
    val user_id: String

)