package com.iprism.ecmhealthadvisor.modals.addleads

data class AddLeadApiResponse(

    val message: String,
    val response: AddLeadResponse,
    val status: Boolean

)

class AddLeadResponse