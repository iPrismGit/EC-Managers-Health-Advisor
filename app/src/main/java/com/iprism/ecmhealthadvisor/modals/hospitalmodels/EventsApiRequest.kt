package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class EventsApiRequest(

    val auth_token: String,
    val camp_place: String,
    val contact_person: String,
    val date: String,
    val event_id: String,
    val event_status: String,
    val image: String,
    val incharge: String,
    val main_data_id: String,
    val organizer: String,
    val page: Int,
    val time: String,
    val type: String,
    val user_id: String,
    val view_type: String

)