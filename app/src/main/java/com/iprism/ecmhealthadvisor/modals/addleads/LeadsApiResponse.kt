package com.iprism.ecmhealthadvisor.modals.addleads

data class LeadsApiResponse(

    val message: String,
    val response: LeadsResponse,
    val status: Boolean

)

data class LeadsResponse(

    val leads: List<Lead>,
    val pagination: Pagination

)

data class Lead(

    val health_advisor_id: String,
    val id: String,
    val image: String,
    val main_data_id: String,
    val mobile: String,
    val name: String,
    val profession: String

)

data class Pagination(

    val current_page: Int,
    val limit: Int,
    val total_pages: List<TotalPage>

)

data class TotalPage(

    val page: Int

)