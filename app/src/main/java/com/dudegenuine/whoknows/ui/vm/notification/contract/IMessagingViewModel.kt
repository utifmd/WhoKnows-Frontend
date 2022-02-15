package com.dudegenuine.whoknows.ui.vm.notification.contract

import com.dudegenuine.model.Messaging

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingViewModel {
    fun getMessagingGroupKey(keyName: String){}
    fun createMessagingGroup(messaging: Messaging.GroupCreator){}
    fun addMessagingGroupMember(messaging: Messaging.GroupAdder){}
    fun pushMessaging(messaging: Messaging.Pusher){}
}