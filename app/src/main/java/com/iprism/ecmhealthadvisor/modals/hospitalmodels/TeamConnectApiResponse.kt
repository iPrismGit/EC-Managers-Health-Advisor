package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class TeamConnectApiResponse(

    val message: String,
    val response: TeamConnectResponse,
    val status: Boolean

)

data class TeamConnectResponse(

    val pagination: Pagination,
    val panel_advisors: List<PanelAdvisor>

)

data class PanelAdvisor(

    val designation: String,
    val id: String,
    val image: String,
    val mobile: String,
    val name: String

)
