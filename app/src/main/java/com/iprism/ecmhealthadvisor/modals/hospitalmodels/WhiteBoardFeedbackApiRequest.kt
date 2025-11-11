package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class WhiteBoardFeedbackApiRequest(

    val auth_token: String,
    val cat_id: String,
    val feedback: String,
    val main_data_id: String,
    val name: String,
    val room_no: String,
    val user_id: String,
    val view_type: String

)