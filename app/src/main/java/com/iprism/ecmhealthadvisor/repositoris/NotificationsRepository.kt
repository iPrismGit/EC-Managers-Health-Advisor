package com.iprism.ecmhealthadvisor.repositoris

import com.iprism.ecmhealthadvisor.modals.notification.NotificationsApiRequest
import com.iprism.ecmhealthadvisor.modals.notification.NotificationsApiResponse
import com.iprism.ecmhealthadvisor.network.EcManagersApi

class NotificationsRepository {


    private val apiService = EcManagersApi.ecManagersService

    suspend fun fetchNotifications(notificationsApiRequest: NotificationsApiRequest): NotificationsApiResponse {
        return apiService.fetchNotifications(notificationsApiRequest)
    }

}