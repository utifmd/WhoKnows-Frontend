package com.dudegenuine.usecase.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dudegenuine.model.Search
import com.dudegenuine.model.common.Utility.DEFAULT_BATCH_SIZE
import com.dudegenuine.repository.contract.IRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Sun, 12 Jun 2022
 * WhoKnows by utifmd
 **/
class SearchRooms
    @Inject constructor(
    private val repository: IRoomRepository){

    /*operator fun invoke(query: String): Flow<PagingData<Room.Censored>> = Pager(
        PagingConfig(DEFAULT_BATCH_SIZE, enablePlaceholders = true)){
        repository.remoteSearchPageCensored(query, DEFAULT_BATCH_SIZE)
    }.flow*/

    operator fun invoke(query: String/*, size: Int*/): Flow<PagingData<Search<*>>> = Pager(
        PagingConfig(DEFAULT_BATCH_SIZE, enablePlaceholders = true)){
        repository.remoteSearchSource(query, DEFAULT_BATCH_SIZE)
    }.flow // change this to livedata
}