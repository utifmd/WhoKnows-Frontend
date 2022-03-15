package com.dudegenuine.usecase.file

import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IFileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Mon, 14 Mar 2022
 * WhoKnows by utifmd
 **/
class DeleteFile
    @Inject constructor(
    private val repository: IFileRepository) {
    operator fun invoke(fileId: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            repository.delete(fileId)
            emit(Resource.Success(fileId))

        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: Resource.THROWABLE_EXCEPTION))
        }
    }
}