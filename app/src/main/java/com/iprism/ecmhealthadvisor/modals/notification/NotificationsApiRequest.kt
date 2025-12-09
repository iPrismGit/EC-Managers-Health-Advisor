package com.iprism.ecmhealthadvisor.modals.notification

data class NotificationsApiRequest(

    val auth_token: String,
    val page: Int,
    val user_id: String,
    val view_type: String,
    val main_data_id: String

)