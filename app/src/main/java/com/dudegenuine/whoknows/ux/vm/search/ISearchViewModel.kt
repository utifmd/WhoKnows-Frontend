package com.dudegenuine.whoknows.ux.vm.search

import androidx.paging.PagingData
import com.dudegenuine.model.Search
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IRoomUseCaseModule
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IUserUseCaseModule
import com.dudegenuine.whoknows.ux.vm.BaseViewModel
import kotlinx.coroutines.flow.Flow

/**
 * Sun, 12 Jun 2022
 * WhoKnows by utifmd
 **/
abstract class ISearchViewModel: BaseViewModel() {
    abstract val currentUserId: String
    abstract val caseRoom: IRoomUseCaseModule
    abstract val caseUser: IUserUseCaseModule
    abstract val pagingFlow: Flow<PagingData<Search<*>>>
    //abstract val pagingFlow: Flow<PagingData<Room.Censored>>
    abstract fun onBackPressed()
    abstract fun onSearchButtonPressed()
}