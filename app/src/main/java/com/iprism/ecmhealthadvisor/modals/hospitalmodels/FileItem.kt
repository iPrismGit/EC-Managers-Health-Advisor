package com.iprism.ecmhealthadvisor.modals.hospitalmodels

import android.net.Uri

class FileItem (

    val uri: Uri,
    val type: FileType

)

enum class FileType {

    IMAGE, PDF

}

