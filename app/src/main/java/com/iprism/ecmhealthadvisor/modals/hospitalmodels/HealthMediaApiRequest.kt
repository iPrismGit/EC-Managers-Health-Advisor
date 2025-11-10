package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class HealthMediaApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val page: Int,
    val user_id: String

)