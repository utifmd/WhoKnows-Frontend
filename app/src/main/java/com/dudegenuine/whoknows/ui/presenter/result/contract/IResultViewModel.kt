package com.dudegenuine.whoknows.ui.presenter.result.contract

import com.dudegenuine.model.Result

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IResultViewModel {
    fun postResult(result: Result)
    fun getResult(id: String)
    fun patchResult(id: String, current: Result)
    fun deleteResult(id: String)
    fun getResults(page: Int, size: Int)
}