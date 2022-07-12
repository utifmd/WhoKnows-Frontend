package com.dudegenuine.usecase.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dudegenuine.model.Room
import com.dudegenuine.model.Search
import com.dudegenuine.model.common.Utility.DEFAULT_BATCH_SIZE
import com.dudegenuine.repository.contract.IRoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Sun, 12 Jun 2022
 * WhoKnows by utifmd
 **/
class SearchRooms
    @Inject constructor(
    private val repository: IRoomRepository){
    private val currentUserId get() = repository.preference.userId

    /*operator fun invoke(query: String): Flow<PagingData<Room.Censored>> = Pager(
        PagingConfig(DEFAULT_BATCH_SIZE, enablePlaceholders = true)){
        repository.remoteSearchPageCensored(query, DEFAULT_BATCH_SIZE)
    }.flow*/

    operator fun invoke(query: String/*, size: Int*/): Flow<PagingData<Search<*>>> = Pager(
        PagingConfig(DEFAULT_BATCH_SIZE, enablePlaceholders = true)){
        repository.remoteSearchSource(query, DEFAULT_BATCH_SIZE)
    }.flow.map{ data -> data.map { search ->
        (search.data as Room.Censored).apply {
            impression = impressions
                .find{ it.userId == currentUserId }
            impressionSize = impressions
                .count{ it.good }
            hasImpressedBefore = impressions
                .any{ it.userId == currentUserId }
            impressed = impressions
                .any{ it.userId == currentUserId && it.good }
            isOwner = userId == currentUserId
        }
        search
    }}
}