package com.dudegenuine.model

import androidx.compose.runtime.mutableStateOf

/**
 * Fri, 11 Feb 2022
 * WhoKnows by utifmd
 **/
sealed class Messaging {
    data class GroupCreator(
        val operation: String = "create",
        val keyName: String,
        val tokens: List<String>): Messaging(){
            var isValid: Boolean = mutableStateOf(
                keyName.isNotBlank() and tokens.isNotEmpty()).value
        }

    data class GroupAdder(
        val operation: String = "add",
        val keyName: String,
        val tokens: List<String>,
        val key: String): Messaging(){
            var isValid: Boolean = mutableStateOf(
                keyName.isNotBlank() and tokens.isNotEmpty() and key.isNotBlank()).value
        }

    data class GroupRemover(
        val operation: String = "remove",
        val keyName: String,
        val tokens: List<String>,
        val key: String): Messaging(){
            var isValid: Boolean = mutableStateOf(
                keyName.isNotBlank() and tokens.isNotEmpty() and key.isNotBlank()).value
        }

    data class Pusher(
        val title: String,
        val body: String,
        val largeIcon: String,
        val to: String): Messaging(){
            var isValid: Boolean = mutableStateOf(
                title.isNotBlank() and body.isNotBlank() and to.isNotBlank()).value
        }

    interface Getter {
        data class Response(
            val notification_key: String = ""){
            var isValid: Boolean = mutableStateOf(notification_key.isNotBlank()).value
        }
    }
}
