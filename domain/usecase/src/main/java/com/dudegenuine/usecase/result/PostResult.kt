package com.dudegenuine.usecase.result

import com.dudegenuine.model.Resource
import com.dudegenuine.model.Result
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.repository.contract.IResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Thu, 09 Dec 2021
 * WhoKnows by utifmd
 **/
class PostResult
    @Inject constructor(
        private val repository: IResultRepository) {
        operator fun invoke(result: Result): Flow<Resource<Result>> = flow {
            try {
                emit(Resource.Loading())
                val posted = repository.create(result)
                emit(Resource.Success(posted))
            } catch (e: HttpFailureException){
                emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
            } catch (e: HttpException){
                emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
            } catch (e: IOException){
                emit(Resource.Error(Resource.IO_EXCEPTION))
            }
        }
}