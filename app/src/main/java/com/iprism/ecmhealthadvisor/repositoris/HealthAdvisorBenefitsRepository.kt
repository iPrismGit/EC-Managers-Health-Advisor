package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.HealthAdvisorBenefitsApiRequest
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.HealthAdvisorBenefitsApiResponse
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiResponse
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class HealthAdvisorBenefitsRepository {

    private val apiService = EcManagersApi.ecManagersService

    suspend fun healthAdvisorBenefits(request: HealthAdvisorBenefitsApiRequest): HealthAdvisorBenefitsApiResponse {
        return apiService.healthAdvisorBenefits(request)
    }

}