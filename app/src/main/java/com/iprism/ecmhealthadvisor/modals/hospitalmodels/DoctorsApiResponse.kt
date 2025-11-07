package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class DoctorsApiResponse(

    val message: String,
    val response: DoctorsResponse,
    val status: Boolean

)

data class DoctorsResponse(

    val doctors: List<Doctor>,
    val pagination: Pagination

)

data class Doctor(

    val auth_token: String,
    val availability: String,
    val caste_community: String,
    val cat_id: String,
    val clinic_fee: String,
    val clinic_name: String,
    val clinic_no: String,
    val consult_type: String,
    val created_on: String,
    val delete_status: String,
    val department: String,
    val description: String,
    val dob: String,
    val email: String,
    val enablex_call_status: String,
    val exp: String,
    val fee: String,
    val gender: String,
    val hod: String,
    val hospital_name: String,
    val hospital_no: String,
    val id: String,
    val image: String,
    val location: String,
    val main_data_id: String,
    val mobile: String,
    val modified_on: String,
    val name: String,
    val no_of_admissions_pm: String,
    val no_of_diagnostic_test_pm: String,
    val no_of_op_day: String,
    val online_consultation: String,
    val online_fee: String,
    val player_id: String,
    val qualification: String,
    val speciality_id: String,
    val specialization: String,
    val status: String,
    val surgery_fee: String,
    val surgery_symptom_id: String,
    val symptom_id: String,
    val symptom_name: String,
    val trade_marketer_id: String,
    val unique_id: String

)