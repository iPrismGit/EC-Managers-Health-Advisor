package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class HospitalFacilitiesApiRequest(

    val auth_token: String,
    val main_data_id: String,
    val page: String,
    val user_id: String

)