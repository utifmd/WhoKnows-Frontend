package com.dudegenuine.remote.service

import com.dudegenuine.remote.entity.QuizEntity
import com.dudegenuine.remote.entity.Response
import com.dudegenuine.remote.service.contract.IQuizService
import com.dudegenuine.remote.service.contract.IQuizService.Companion.ENDPOINT
import com.dudegenuine.remote.service.contract.IQuizService.Companion.HEADER
import retrofit2.http.*

/**
 * Wed, 08 Dec 2021
 * WhoKnows by utifmd
 **/
interface QuizService: IQuizService {
    @Headers("X-Api-Key: utif.pages.dev", "Content-Type: application/json", "Accept: application/json")
    @POST(ENDPOINT)
    override suspend fun create(
        @Body entity: QuizEntity): Response<QuizEntity>

    @Headers(HEADER)
    @GET("${ENDPOINT}/{quizId}")
    override suspend fun read(
        @Path("quizId") id: String): Response<QuizEntity>

    @Headers(HEADER)
    @PATCH("${ENDPOINT}/{quizId}")
    override suspend fun update(
        @Path("quizId") id: String,
        @Body entity: QuizEntity): Response<QuizEntity>

    @Headers(HEADER)
    @DELETE("${ENDPOINT}/{quizId}")
    override suspend fun delete(
        @Path("quizId") id: String)

    @Headers(HEADER)
    @GET(ENDPOINT)
    override suspend fun list(
        @Query("page") page: Int,
        @Query("size") size: Int): Response<List<QuizEntity>>
}