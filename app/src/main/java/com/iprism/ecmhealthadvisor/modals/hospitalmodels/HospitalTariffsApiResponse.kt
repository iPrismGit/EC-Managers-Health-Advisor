package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import com.iprism.ecmhealthadvisor.modals.addleads.Pagination

data class HospitalTariffsApiResponse(

    val message: String,
    val response: HospitalTariffsResponse,
    val status: Boolean

)

data class HospitalTariffsResponse(

    val categories: List<TariffCategory>,
    val pagination: Pagination,
    val tariffs: List<Tariff>

)

data class TariffCategory(

    val id: String,
    val image: String,
    val name: String

)

data class Tariff(

    val from_price: Int,
    val id: String,
    val image: String,
    val main_data_id: String,
    val name: String,
    val to_price: Int

)