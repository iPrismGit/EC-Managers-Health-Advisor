package com.iprism.ecmhealthadvisor.modals.authentication

data class LoginApiResponse(

    val message: String,
    val response: Response,
    val status: Boolean

)

data class Response(

    val address: String,
    val auth_token: String,
    val availability: String,
    val blood_group: String,
    val cat_id: String,
    val corporate_marketer_id: String,
    val created_on: String,
    val crm_admin_id: String,
    val delete_status: String,
    val designation: String,
    val device_token: String,
    val dob: String,
    val email: String,
    val gender: String,
    val gm_id: String,
    val health_club_task: String,
    val hospital_name: String,
    val id: String,
    val image: String,
    val inbound_marketer_id: String,
    val ios_version: String,
    val lat: String,
    val lon: String,
    val m_id: String,
    val main_data_id: String,
    val mm_id: String,
    val mobile: String,
    val modified_on: String,
    val name: String,
    val outbound_marketer_id: String,
    val player_id: String,
    val status: String,
    val sub_cat_id: String,
    val trade_marketer_id: String,
    val type: String,
    val unique_id: String

)