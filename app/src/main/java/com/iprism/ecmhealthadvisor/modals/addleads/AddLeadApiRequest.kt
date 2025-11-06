package com.iprism.ecmhealthadvisor.modals.addleads

data class AddLeadApiRequest(

    val address: String,
    val auth_token: String,
    val dob: String,
    val email: String,
    val gender: String,
    val bloodGroup : String,
    val image: String,
    val insurence_company_name: String,
    val lat: String,
    val lead_type: String,
    val lon: String,
    val main_data_id: String,
    val mobile: String,
    val name: String,
    val no_of_persons_covered: String,
    val payment_type: String,
    val payment_type_category: String,
    val profession: String,
    val total_family_members: String,
    val tpa_name: String,
    val treatment_status: String,
    val user_id: String

)