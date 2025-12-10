package com.iprism.ecmhealthadvisor.modals.toprequests

data class RequestAndReferApiResponse(

    val message: String,
    val response: RequestAndReferResponse,
    val status: Boolean

)

data class RequestAndReferResponse(

    val id: String,
    val mobile: String

)