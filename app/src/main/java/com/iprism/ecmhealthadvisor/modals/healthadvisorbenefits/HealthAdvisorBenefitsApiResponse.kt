package com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits

data class HealthAdvisorBenefitsApiResponse(

    val message: String,
    val response: List<BenefitsResponse>,
    val status: Boolean
)

data class BenefitsResponse(

    val id: String,
    val image: String,
    val name: String

)