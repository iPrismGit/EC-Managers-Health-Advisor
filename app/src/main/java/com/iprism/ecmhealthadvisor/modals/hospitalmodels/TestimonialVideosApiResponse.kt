package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class TestimonialVideosApiResponse(

    val message: String,
    val response: TestimonialVideosResponse,
    val status: Boolean

)

data class TestimonialVideosResponse(

    val pagination: Pagination,
    val videos: List<Video>

)

data class Video(

    val id: String,
    val image: String,
    val link: String,
    val main_data_id: String

)