package com.iprism.ecmhealthadvisor.modals.homepagemodels

import com.google.gson.annotations.SerializedName

data class HomePageApiResponse(

    val message: String,
    val response: HomePageResponse,
    val status: Boolean

)

data class HomePageResponse(

    @field:SerializedName("bottom_banners")
    val bottom_banners: List<Banner>,
    @field:SerializedName("middle_banners")
    val middle_banners: List<Banner>,
    @field:SerializedName("top_banners")
    val top_banners: List<Banner>

)

data class Banner(

    val id: String,
    val image: String

)
