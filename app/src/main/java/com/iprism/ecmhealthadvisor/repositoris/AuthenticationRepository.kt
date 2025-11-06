package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.authentication.LoginApiRequest
import com.iprism.ecmhealthadvisor.modals.authentication.LoginApiResponse
import com.iprism.ecmhealthadvisor.modals.authentication.ResendOtpApiRequest
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class AuthenticationRepository {

    private val apiService = EcManagersApi.ecManagersService

    suspend fun login(loginRequest: LoginApiRequest): LoginApiResponse {
        return apiService.login(loginRequest)
    }

    suspend fun resendOtp(request: ResendOtpApiRequest): LoginApiResponse {
        return apiService.resendOtp(request)
    }

}