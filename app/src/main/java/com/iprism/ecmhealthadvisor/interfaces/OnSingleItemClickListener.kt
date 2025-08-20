package com.iprism.ecmcorporatemarketing.interfaces

interface OnSingleItemClickListener {

    fun onCallNowClick(doctorId : String, mobile : String)

    fun onSmsClick(doctorId : String, mobile : String)

    fun onWhatsappClick(doctorId : String, mobile : String)

}