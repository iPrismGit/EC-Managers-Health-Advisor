package com.iprism.ecmhealthadvisor.modals.rewards

data class RewardsApiResponse (

    val message: String,
    val response: RewardsResponse,
    val status: Boolean

)

data class RewardsResponse (

    val this_week: Int,
    val today: Int,
    val total_commission: Int,
    val total_credited: Int,
    val total_requested: Int,
    val wallet_balance: Int,
    val yesterday: Int

)