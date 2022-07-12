package com.dudegenuine.whoknows.ux.vm.result.contract

import com.dudegenuine.model.Result

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
interface IResultViewModel {
    companion object {

        const val RESULT_USER_ID_SAVED_KEY = "result_user_id_saved_key"
        const val RESULT_ROOM_ID_SAVED_KEY = "result_room_id_saved_key"
        const val RESULT_ACTION_SAVED_KEY = "result_action_saved_key"

    }
    fun postResult(result: Result)
    fun getResult(id: String)
    fun patchResult(id: String, current: Result)
    fun deleteResult(id: String)
    fun getResults(page: Int, size: Int)
}