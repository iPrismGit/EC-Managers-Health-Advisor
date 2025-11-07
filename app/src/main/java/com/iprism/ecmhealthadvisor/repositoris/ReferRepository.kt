package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiResponse
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferApiRequest
import com.iprism.ecmhealthadvisor.modals.referadmissionanddiagnostic.ReferApiResponse
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class ReferRepository {

    private val apiService = EcManagersApi.ecManagersService

    suspend fun referAdmissionOrDiagnostic(request: ReferApiRequest): ReferApiResponse {
        return apiService.referAdmissionOrDiagnostic(request)
    }

}