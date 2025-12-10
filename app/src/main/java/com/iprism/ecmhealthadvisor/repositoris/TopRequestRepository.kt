package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.toprequests.ReferAnAdmissionApiRequest
import com.iprism.ecmhealthadvisor.modals.toprequests.ReferDiagnosticApiRequest
import com.iprism.ecmhealthadvisor.modals.toprequests.RequestAndReferApiResponse
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class TopRequestRepository {

    private val apiService = EcManagersApi.ecManagersService



    suspend fun referAdmission(request: ReferAnAdmissionApiRequest): RequestAndReferApiResponse {
        return apiService.referAdmission(request)
    }

    suspend fun referDiagnostic(request: ReferDiagnosticApiRequest): RequestAndReferApiResponse {
        return apiService.referDiagnostic(request)
    }

}