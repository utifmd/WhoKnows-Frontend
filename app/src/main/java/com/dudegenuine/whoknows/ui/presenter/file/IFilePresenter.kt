package com.dudegenuine.whoknows.ui.presenter.file

import com.dudegenuine.model.File
import com.dudegenuine.model.Resource

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
interface IFilePresenter {
    fun uploadFile(byteArray: ByteArray)
    fun onFileUploaded(resource: Resource<File>)
}