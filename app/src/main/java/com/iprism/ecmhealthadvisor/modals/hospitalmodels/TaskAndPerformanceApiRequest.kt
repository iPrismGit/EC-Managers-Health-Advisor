package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class TaskAndPerformanceApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val month: String,
    val type: String,
    val user_id: String,
    val view_type: String,
    val year: String

)