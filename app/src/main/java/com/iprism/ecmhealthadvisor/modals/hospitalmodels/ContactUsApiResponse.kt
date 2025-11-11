package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class ContactUsApiResponse(

    val message: String,
    val response: ContactUsResponse,
    val status: Boolean

)

data class ContactUsResponse(

    val mobile: Long

)