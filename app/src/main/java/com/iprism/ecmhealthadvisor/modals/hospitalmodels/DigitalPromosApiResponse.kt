package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class DigitalPromosApiResponse(

    val message: String,
    val response: DigitalPromosResponse,
    val status: Boolean

)

data class DigitalPromosResponse(

    val digital_promos: List<DigitalPromo>,
    val pagination: Pagination

)

data class DigitalPromo(

    val id: String,
    val media: List<Media>,
    val name: String
)

