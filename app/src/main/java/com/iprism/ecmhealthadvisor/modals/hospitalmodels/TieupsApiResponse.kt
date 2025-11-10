package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class TieupsApiResponse(

    val message: String,
    val response: TieupsResponse,
    val status: Boolean

)

data class TieupsResponse(

    val pagination: Pagination,
    val tieups: List<Tieup>

)

data class Tieup(

    val id: String,
    val media: List<Media>,
    val name: String

)