package com.dudegenuine.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dudegenuine.model.Resource.Companion.IO_EXCEPTION

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
    }
}

class ResourcePaging<T: Any>(
    private val onEvent: suspend (Int) -> List<T>): PagingSource<Int, T>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val pageNumber = params.key ?: 0
        val list = onEvent(pageNumber)

        if (list.isNotEmpty()) LoadResult.Page(
            data = list,
            prevKey = if (pageNumber > 0) pageNumber -1 else null,
            nextKey = if (list.isNotEmpty()) pageNumber +1 else null

        ) else LoadResult.Error(Throwable(Resource.NO_RESULT))
    } catch (e: Exception) {
        LoadResult.Error(Throwable(IO_EXCEPTION))
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = // null
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
}