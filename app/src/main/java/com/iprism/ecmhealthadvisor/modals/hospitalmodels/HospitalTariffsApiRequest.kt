package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class HospitalTariffsApiRequest(

    val auth_token: String,
    val cat_id: String,
    val main_data_id: String,
    val page: Int,
    val user_id: String,
    val view_type: String

)