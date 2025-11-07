package com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic

data class ReferApiResponse(

    val message: String,
    val response: ReferResponse,
    val status: Boolean

)

class ReferResponse