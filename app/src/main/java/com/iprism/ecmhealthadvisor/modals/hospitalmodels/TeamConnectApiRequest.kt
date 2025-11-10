package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class TeamConnectApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val page: Int,
    val type: String,
    val user_id: String

)