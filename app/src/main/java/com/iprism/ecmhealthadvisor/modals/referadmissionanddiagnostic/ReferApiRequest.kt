package com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic

data class ReferApiRequest(

    val auth_token: String,
    val dob: String,
    val gender: String,
    val main_data_id: String,
    val mobile: String,
    val name: String,
    val refer_type: String,
    val user_id: String

)