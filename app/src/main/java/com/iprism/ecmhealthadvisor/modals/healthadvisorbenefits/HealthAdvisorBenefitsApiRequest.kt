package com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits

data class HealthAdvisorBenefitsApiRequest(

    val auth_token: String,
    val cat_id: String,
    val main_data_id: String,
    val sub_cat_id: String,
    val user_id: String,
    val view_type: String

)