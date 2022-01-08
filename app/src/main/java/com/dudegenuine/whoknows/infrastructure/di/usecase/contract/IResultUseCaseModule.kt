package com.dudegenuine.whoknows.infrastructure.di.usecase.contract

import com.dudegenuine.usecase.result.*

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IResultUseCaseModule {
    val postResult: PostResult
    val getResult: GetResult
    val patchResult: PatchResult
    val deleteResult: DeleteResult
    val getResults: GetResults
}