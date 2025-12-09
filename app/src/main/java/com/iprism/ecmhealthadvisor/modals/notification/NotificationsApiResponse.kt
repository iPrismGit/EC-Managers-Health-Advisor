package com.iprism.ecmhealthadvisor.modals.notification

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class NotificationsApiResponse(

    val message: String,
    val response: NotificationsResponse,
    val status: Boolean

)

data class NotificationsResponse(
    val count : Int,
    val notifications: List<Notification>,
    val pagination: Pagination

)

data class Notification(

    val created_on: String,
    val id: String,
    val message: String,
    val read_status: Int,
    val title: String

)
