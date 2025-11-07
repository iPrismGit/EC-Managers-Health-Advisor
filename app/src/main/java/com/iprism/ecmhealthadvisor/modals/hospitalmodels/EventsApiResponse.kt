package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class EventsApiResponse(

    val message: String,
    val response: EventsResponse,
    val status: Boolean

)

data class EventsResponse(

    val events: List<Event>,
    val pagination: Pagination

)

data class Event(

    val camp_place: String,
    val contact_person: String,
    val date: String,
    val event_status: String,
    val id: String,
    val image: String,
    val incharge: String,
    val main_data_id: String,
    val organizer: String,
    val time: String,
    val trade_marketer_id: String,
    val type: String

)

