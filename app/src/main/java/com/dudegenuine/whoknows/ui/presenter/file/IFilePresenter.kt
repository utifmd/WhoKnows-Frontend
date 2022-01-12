package com.dudegenuine.whoknows.ui.presenter.file

import com.dudegenuine.model.File
import com.dudegenuine.model.Resource

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFilePresenter {
    fun singleUpload(byteArray: ByteArray){ }
    fun multiUpload(byteArrays: List<ByteArray>){ }

    fun onSingleUploaded(resource: Resource<File>){ }
    fun onMultiUploaded(resources: Resource<List<File>>){ }
}