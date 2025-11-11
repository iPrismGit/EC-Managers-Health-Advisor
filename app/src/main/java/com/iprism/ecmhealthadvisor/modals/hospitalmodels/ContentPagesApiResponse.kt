package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class ContentPagesApiResponse(

    val message: String,
    val response: ContentPagesResponse,
    val status: Boolean

)

data class ContentPagesResponse(

    val name: String

)