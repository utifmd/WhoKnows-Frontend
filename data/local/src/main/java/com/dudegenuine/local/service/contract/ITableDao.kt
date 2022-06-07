package com.dudegenuine.local.service.contract

/**
 * Wed, 01 Jun 2022
 * WhoKnows by utifmd
 **/
interface ITableDao<T> {
    suspend fun create(item: T)
    suspend fun createAll(list: List<T>)
    suspend fun read(param: String): T?
    suspend fun update(item: T)
    suspend fun delete(model: T)
    suspend fun list(): List<T>
    suspend fun list(param: String, batchSize: Int): List<T>
    suspend fun trim(batchSize: Int)
    suspend fun clear()
}