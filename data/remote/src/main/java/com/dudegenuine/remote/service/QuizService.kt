package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.IQuizService
import com.dudegenuine.remote.service.contract.IQuizService.Companion.ACCEPT
import com.dudegenuine.remote.service.contract.IQuizService.Companion.API_KEY
import com.dudegenuine.remote.service.contract.IQuizService.Companion.CONTENT_TYPE
import com.dudegenuine.remote.service.contract.IQuizService.Companion.ENDPOINT
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface QuizService: IQuizService {
    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: QuizEntity): Response<QuizEntity>

    @Headers(API_KEY, ACCEPT)
    @GET("${ENDPOINT}/{quizId}")
    override suspend fun read(
        @Path("quizId") id: String): Response<QuizEntity>

    @Headers(API_KEY, CONTENT_TYPE, ACCEPT)
    @PUT("${ENDPOINT}/{quizId}")
    override suspend fun update(
        @Path("quizId") id: String,
        @Body entity: QuizEntity): Response<QuizEntity>

    @Headers(API_KEY, ACCEPT)
    @DELETE("${ENDPOINT}/{quizId}")
    override suspend fun delete(
        @Path("quizId") id: String)

    @Headers(API_KEY, ACCEPT)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<QuizEntity>>
}