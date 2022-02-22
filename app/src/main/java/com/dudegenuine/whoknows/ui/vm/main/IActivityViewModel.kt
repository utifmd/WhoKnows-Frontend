package com.dudegenuine.whoknows.ui.vm.main

import com.dudegenuine.whoknows.ui.vm.notification.contract.IMessagingViewModel

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IActivityViewModel: IMessagingViewModel {
    //fun getMessagingGroupKey(keyName: String, onSucceed: (String) -> Unit)
    fun getJoinedRoomIds(onSucceed: (List<String>) -> Unit)
}