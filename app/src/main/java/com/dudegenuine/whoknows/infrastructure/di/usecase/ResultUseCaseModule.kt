package com.dudegenuine.whoknows.infrastructure.di.usecase

import com.dudegenuine.repository.contract.IResultRepository
import com.dudegenuine.usecase.result.*
import com.dudegenuine.whoknows.infrastructure.di.usecase.contract.IResultUseCaseModule

/**
 * Sat, 08 Jan 2022
 * WhoKnows by utifmd
 **/
class ResultUseCaseModule(
    private val repository: IResultRepository,

    override val postResult: PostResult =
        PostResult(repository),

    override val getResult: GetResult =
        GetResult(repository),

    override val patchResult: PatchResult =
        PatchResult(repository),

    override val deleteResult: DeleteResult =
        DeleteResult(repository),

    override val getResults: GetResults =
        GetResults(repository)

) : IResultUseCaseModule