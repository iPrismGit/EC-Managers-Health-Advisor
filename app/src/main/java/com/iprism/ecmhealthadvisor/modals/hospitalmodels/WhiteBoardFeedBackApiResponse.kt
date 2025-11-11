package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class WhiteBoardFeedBackApiResponse(

    val message: String,
    val response: WhiteBoardFeedBackResponse,
    val status: Boolean

)

data class WhiteBoardFeedBackResponse(

    val categories: List<WhiteBoardCategory>

)

data class WhiteBoardCategory(

    val id: String,
    val image: String,
    val name: String

)