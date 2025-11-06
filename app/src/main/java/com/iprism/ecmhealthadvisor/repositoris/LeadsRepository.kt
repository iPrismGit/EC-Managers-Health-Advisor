package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.addleads.AddLeadApiRequest
import com.iprism.ecmhealthadvisor.modals.addleads.AddLeadApiResponse
import com.iprism.ecmhealthadvisor.modals.addleads.LeadsApiRequest
import com.iprism.ecmhealthadvisor.modals.addleads.LeadsApiResponse
import com.iprism.ecmhealthadvisor.modals.addleads.UserDropDownsApiResponse
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class LeadsRepository {

    private val apiService = EcManagersApi.ecManagersService

    suspend fun fetchUserDropDowns(): UserDropDownsApiResponse {
        return apiService.fetchUserDropDowns()
    }

    suspend fun addLead(request: AddLeadApiRequest): AddLeadApiResponse {
        return apiService.addLead(request)
    }

    suspend fun fetchAllLeads(request: LeadsApiRequest): LeadsApiResponse {
        return apiService.fetchTotalLeads(request)
    }


}