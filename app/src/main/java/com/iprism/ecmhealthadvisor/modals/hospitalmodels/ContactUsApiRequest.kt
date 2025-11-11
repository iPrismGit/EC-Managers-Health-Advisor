package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class ContactUsApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val email: String,
    val message: String,
    val mobile: String,
    val name: String,
    val user_id: String,
    val view_type: String

)