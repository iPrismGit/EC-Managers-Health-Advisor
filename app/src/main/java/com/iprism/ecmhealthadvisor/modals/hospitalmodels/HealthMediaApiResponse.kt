package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class HealthMediaApiResponse(

    val message: String,
    val response: HealthMediaResponse,
    val status: Boolean

)

data class HealthMediaResponse(

    val main_data: List<MainData>,
    val pagination: Pagination

)

data class MainData(

    val created_on: String,
    val id: String,
    val image: String

)
