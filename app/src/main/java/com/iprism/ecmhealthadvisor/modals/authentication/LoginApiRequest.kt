package com.iprism.ecmhealthadvisor.modals.authentication

data class LoginApiRequest(

    val device_token: String,
    val ios_version: String,
    val mobile: String,
    val otp_status: String,
    val player_id: String

)