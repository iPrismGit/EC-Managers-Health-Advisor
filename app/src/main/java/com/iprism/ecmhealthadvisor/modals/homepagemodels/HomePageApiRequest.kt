package com.iprism.ecmhealthadvisor.modals.homepagemodels

data class HomePageApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val player_id: String,
    val user_id: String

)