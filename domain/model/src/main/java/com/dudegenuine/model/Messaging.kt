package com.dudegenuine.model

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
sealed class Messaging {
    data class GroupCreator(
        val operation: String = "create",
        val keyName: String,
        val tokens: List<String>): Messaging()

    data class GroupAdder(
        val operation: String = "add",
        val keyName: String,
        val tokens: List<String>,
        val key: String): Messaging()

    data class Pusher(
        val title: String,
        val body: String,
        val to: String): Messaging()

    interface Getter {
        data class Response(
            val notification_key: String = ""
        )
    }
}
