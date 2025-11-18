package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.rewards.RewardsApiRequest
import com.iprism.ecmhealthadvisor.modals.rewards.RewardsApiResponse
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class RewardsRepository {

    private val apiService = EcManagersApi.ecManagersService


    suspend fun fetchRewards(request: RewardsApiRequest): RewardsApiResponse {
        return apiService.fetchRewards(request)
    }

}