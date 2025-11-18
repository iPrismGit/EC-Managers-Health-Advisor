package com.iprism.ecmhealthadvisor.modals.rewards

data class RewardsApiRequest(

    val amount: Int,
    val auth_token: String,
    val main_data_id: String,
    val user_id: String,
    val view_type: String,
    val wallet_balance: Int

)