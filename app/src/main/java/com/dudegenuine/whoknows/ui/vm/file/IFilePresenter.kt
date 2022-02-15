package com.dudegenuine.whoknows.ui.vm.file

import com.dudegenuine.model.File
import com.dudegenuine.model.Resource

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFilePresenter {
    fun singleUpload(byteArray: ByteArray){ }
    fun <T> singleUpload(byteArray: ByteArray, onSucceed: (T) -> Unit){ }

    fun multiUpload(byteArrays: List<ByteArray>){ }
    fun <T> multiUpload(byteArrays: List<ByteArray>, onSucceed: (T) -> Unit){ }

    fun onSingleUploaded(resource: Resource<File>){ }
    fun <T> onSingleUploaded(resource: Resource<File>, onSucceed: (T) -> Unit){ }

    fun onMultiUploaded(resources: Resource<List<File>>){ }
    fun <T> onMultiUploaded(resources: Resource<List<File>>, onSucceed: (T) -> Unit){ }
}