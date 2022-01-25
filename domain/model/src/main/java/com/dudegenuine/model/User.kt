package com.dudegenuine.model

import java.io.Serializable
import java.util.*

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/

data class User (
    val id: String,
    var fullName: String,
    var email: String,
    var phone: String,
    var username: String,
    var password: String,
    var profileUrl: String,
    var createdAt: Date,
    var updatedAt: Date?): Serializable {
    val isPropsBlank: Boolean =
        /*fullName.isBlank() ||*/ email.isBlank() || /*phone.isBlank() ||*/
                username.isBlank() || password.isBlank()

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"

        object KeyChanger {
            const val NAME = "Full Name"
            const val PHONE = "Phone Number"
            const val EMAIL = "Email Address"
        }
    }
}
