package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiRequest
import com.iprism.ecmhealthadvisor.modals.homepagemodels.HomePageApiResponse

import com.iprism.ecmhealthadvisor.network.EcManagersApi

class HomePageRepository {

    private val apiService = EcManagersApi.ecManagersService

    suspend fun fetchHomePageBanners(request: HomePageApiRequest): HomePageApiResponse {
        return apiService.fetchHomePageResponse(request)
    }

}