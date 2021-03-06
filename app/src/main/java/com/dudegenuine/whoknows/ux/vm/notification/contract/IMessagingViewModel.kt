package com.dudegenuine.whoknows.ux.vm.notification.contract

import com.dudegenuine.model.Messaging

/**
 * Mon, 14 Feb 2022
 * WhoKnows by utifmd
 **/
interface IMessagingViewModel {
    companion object{
        const val DEFAULT_NOTIFIER_BATCH_SIZE = 7
    }

    fun createMessaging(messaging: Messaging.GroupCreator, onSucceed: (String) -> Unit){}
    fun getMessaging(keyName: String, onSucceed: (String) -> Unit){}
    fun addMessaging(messaging: Messaging.GroupAdder, onSucceed: (String) -> Unit){}
    fun removeMessaging(messaging: Messaging.GroupRemover, onSucceed: (String) -> Unit){}
    fun pushMessaging(messaging: Messaging.Pusher, onSucceed: (String) -> Unit){}

    /*fun getMessagingGroupKey(keyName: String){}
    fun createMessagingGroup(messaging: Messaging.GroupCreator){}
    fun addMessagingGroupMember(messaging: Messaging.GroupAdder){}
    fun removeGroupMemberMessaging(messaging: Messaging.GroupRemover){}
    fun pushMessaging(messaging: Messaging.Pusher){}*/
}