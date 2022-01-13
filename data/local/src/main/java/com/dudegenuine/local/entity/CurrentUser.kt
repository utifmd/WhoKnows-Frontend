package com.dudegenuine.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Thu, 13 Jan 2022
 * WhoKnows by utifmd
 **/
@Entity
data class CurrentUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "fullName")
    val fullName: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: Date,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: Date,
)
