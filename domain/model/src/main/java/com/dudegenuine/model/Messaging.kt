package com.dudegenuine.model

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
sealed class Messaging {
    data class GroupCreator(
        val keyName: String,
        val tokens: List<String>,
        val operation: String = CREATE): Messaging()

    data class GroupAdder(
        val keyName: String,
        val tokens: List<String>,
        val key: String = "",
        val operation: String = ADD): Messaging()

    data class GroupRemover(
        val keyName: String,
        val tokens: List<String>,
        val key: String,
        val operation: String = REMOVE): Messaging()

    data class Pusher(
        val title: String,
        val body: String,
        val largeIcon: String,
        val args: String = "",
        val to: String = ""): Messaging()

    interface Getter {
        data class Response(val notification_key: String = "", val error: String? = "")
    }
    companion object{
        const val CREATE = "create"
        const val ADD = "add"
        const val REMOVE = "remove"
    }
}
