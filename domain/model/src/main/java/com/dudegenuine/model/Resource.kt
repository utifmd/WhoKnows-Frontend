package com.dudegenuine.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dudegenuine.model.common.validation.HttpFailureException
import retrofit2.HttpException
import java.io.IOException

/**
 * Wed, 01 Dec 2021
 * WhoKnows by utifmd
 **/
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null ) {

    class Success<T>(data: T): Resource<T> (
        data = data
    )
    class Error<T>(message: String, data: T? = null): Resource<T> (
        message = message,
        data = data
    )
    class Loading<T>(data: T? = null): Resource<T> (
        data = data
    )

    companion object {
        const val HTTP_EXCEPTION = "An expected http exception error occurred."
        const val HTTP_FAILURE_EXCEPTION = "Server response failure."
        const val THROWABLE_EXCEPTION = "An expected error occurred."
        const val SOCKET_TIMEOUT_EXCEPTION = "Timeout - Please check your internet connection"
        const val UNKNOWN_HOST_EXCEPTION = "Unable to make a connection. Please check your internet"
        const val CONN_SHUT_DOWN_EXCEPTION = "Connection shutdown. Please check your internet"
        const val IO_EXCEPTION = "Server is unreachable, please try again later."
        const val ILLEGAL_STATE_EXCEPTION = "An expected error occurred."
        const val NO_RESULT = "No result."
        const val NOT_FOUND_EXCEPTION = "Not Found Exception"
        const val KEY_REFRESH = "key_refresh"
        //const val KEY_USER_ID = "key_user_id"
    }
}

class ResourcePaging<T: Any>(
    private val onEvent: suspend (pageNumber: Int) -> List<T>): PagingSource<Int, T>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val serverStartingIndex = 0
        val pageNumber = params.key ?: serverStartingIndex
        val list = onEvent(pageNumber)

        LoadResult.Page(
            data = list,
            prevKey = if (pageNumber > serverStartingIndex) pageNumber -1 else null,
            nextKey = if (list.isNotEmpty()) pageNumber +1 else null
        )
    } catch (e: HttpFailureException){
        LoadResult.Error(Throwable(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
    } catch (e: HttpException){
        LoadResult.Error(Throwable(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
    } catch (e: IOException){
        LoadResult.Error(Throwable(Resource.IO_EXCEPTION))
    } catch (e: Exception){
        LoadResult.Error(Throwable(e.localizedMessage ?: Resource.THROWABLE_EXCEPTION))
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = // null
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
}

/*
class ResourceMediator<T: Any>(
    private val onLocal: suspend (Int) -> List<T>,
    private val onRemote: suspend (Int) -> List<T>): PagingSource<Int, T>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val pageNumber = params.key ?: 0
        val local = onLocal(pageNumber)
        val remote = onRemote(pageNumber)

        if (remote.isNotEmpty()) LoadResult.Page(
            data = remote,
            prevKey = if (pageNumber > 0) pageNumber -1 else null,
            nextKey = if (remote.isNotEmpty()) pageNumber +1 else null

        ) else LoadResult.Error(Throwable(Resource.NO_RESULT))
    } catch (e: Exception) {
        LoadResult.Error(Throwable(IO_EXCEPTION))
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = // null
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
}*/
