package com.iprism.ecmhealthadvisor.modals.hospitalmodels

data class TaskAndPerformanceApiResponse(

    val message: String,
    val response: TaskAndPerformanceResponse,
    val status: Boolean

)

data class TaskAndPerformanceResponse(

    val achieved: Int,
    val lead_task: Int,
    val pending: Int

)