package com.iprism.ecmhealthadvisor.modals.addleads

data class LeadsApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val page: Int,
    val user_id: String,
    val user_type: String

)