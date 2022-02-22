package com.dudegenuine.whoknows.ui.vm.notification.contract

import com.dudegenuine.model.Messaging

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingViewModel {
    fun createMessagingGroup(messaging: Messaging.GroupCreator, onSucceed: (String) -> Unit){}
    fun getMessagingGroupKey(keyName: String, onSucceed: (String) -> Unit){}
    fun addMessagingGroupMember(messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit){}
    fun removeGroupMemberMessaging(messaging: Messaging.GroupRemover, onSucceed: (String) -> Unit){}
    fun pushMessaging(messaging: Messaging.Pusher, onSucceed: (String) -> Unit){}

    /*fun getMessagingGroupKey(keyName: String){}
    fun createMessagingGroup(messaging: Messaging.GroupCreator){}
    fun addMessagingGroupMember(messaging: Messaging.GroupAdder){}
    fun removeGroupMemberMessaging(messaging: Messaging.GroupRemover){}
    fun pushMessaging(messaging: Messaging.Pusher){}*/
}