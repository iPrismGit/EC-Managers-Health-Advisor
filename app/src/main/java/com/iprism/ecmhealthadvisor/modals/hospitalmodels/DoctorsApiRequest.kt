package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class DoctorsApiRequest(

    val auth_token: String,
    val cat_id: String,
    val main_data_id: String,
    val page: Int,
    val speciality_id: String,
    val user_id: String

)