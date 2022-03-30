package com.dudegenuine.whoknows.ui.vm.file

import androidx.lifecycle.SavedStateHandle
import com.dudegenuine.local.api.IShareLauncher
import com.dudegenuine.whoknows.BuildConfig
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IFileUseCaseModule
import com.dudegenuine.whoknows.ui.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Tue, 15 Mar 2022
 * WhoKnows by utifmd
 **/
@HiltViewModel
class FileViewModel
    @Inject constructor(
    private val caseFile: IFileUseCaseModule,
    private val caseShare: IShareLauncher,
    private val savedStateHandle: SavedStateHandle): BaseViewModel(), IFileViewModel {

    fun onSharePressed(fileId: String){
        val data = "${BuildConfig.BASE_CLIENT_URL}/who-knows/image-viewer/$fileId"
        caseShare.launch(data)
    }
}