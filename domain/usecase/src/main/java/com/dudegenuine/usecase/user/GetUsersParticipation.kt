package com.dudegenuine.usecase.user

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dudegenuine.model.User
import com.dudegenuine.repository.contract.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Thu, 24 Mar 2022
 * WhoKnows by utifmd
 **/
class GetUsersParticipation
    @Inject constructor(
    private val repository: IUserRepository) {

    operator fun invoke(size: Int): Flow<PagingData<User.Censored>> {
        val config = PagingConfig(size, enablePlaceholders = true)
        val pager = Pager(config) { repository.page(size) }

        return pager.flow
    }
}