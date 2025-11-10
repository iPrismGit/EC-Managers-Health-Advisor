package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class HospitalFacilitiesApiResponse(

    val message: String,
    val response: HospitalFacilitiesResponse,
    val status: Boolean

)

data class HospitalFacilitiesResponse(

    val facilities: List<Facility>,
    val pagination: Pagination

)

data class Facility(

    val id: String,
    val media: List<Media>,
    val name: String

)

data class Media(
    val type: String,
    val url: String
)

