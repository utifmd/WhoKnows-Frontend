package com.dudegenuine.usecase.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dudegenuine.model.Search
import com.dudegenuine.model.common.Utility.DEFAULT_BATCH_SIZE
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Tue, 14 Jun 2022
 * WhoKnows by utifmd
 **/
class SearchUser
    @Inject constructor(
    private val repository: IUserRepository) {

    @OptIn(FlowPreview::class)
    operator fun invoke(query: String): Flow<PagingData<Search<*>>> = Pager(
        config = PagingConfig(pageSize = DEFAULT_BATCH_SIZE, enablePlaceholders = true)){

        repository.remoteSearchSource(query, DEFAULT_BATCH_SIZE)
    }.flow
}