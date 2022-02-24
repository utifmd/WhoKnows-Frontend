package com.dudegenuine.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

/**
 * Thu, 24 Feb 2022
 * WhoKnows by utifmd
 **/
class ResourcePaging<T: Any>(
    private val onEvent: suspend (Int) -> List<T>): PagingSource<Int, T>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val pageNumber = params.key ?: 0
        val list = onEvent(pageNumber)

        LoadResult.Page(
            data = list,
            prevKey = if (pageNumber > 0) pageNumber -1 else null,
            nextKey = if (list.isNotEmpty()) pageNumber +1 else null
        )

    } catch (e: IOException) {
        LoadResult.Error(Throwable(Resource.IO_EXCEPTION))

    } catch (e: HttpException) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
}