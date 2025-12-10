package com.iprism.ecmhealthadvisor.modals.toprequests

data class ReferDiagnosticApiRequest(

    val age: String,
    val auth_token: String,
    val image: String,
    val main_data_id: String,
    val mobile: String,
    val name: String,
    val test: String,
    val user_id: String

)