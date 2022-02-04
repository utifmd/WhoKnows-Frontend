package com.dudegenuine.usecase.file

import android.content.Context
import com.dudegenuine.model.File
import com.dudegenuine.model.Resource
import com.dudegenuine.model.common.validation.HttpFailureException
import com.dudegenuine.repository.contract.IFileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Tue, 11 Jan 2022
 * WhoKnows by utifmd
 **/
class UploadFiles

    @Inject constructor(
    val context: Context,
    val repository: IFileRepository) {

    operator fun invoke(byteArrays: List<ByteArray>): Flow<Resource<List<File>>> = flow {
        try {
            emit(Resource.Loading())
            val data = repository.upload(byteArrays)
            emit(Resource.Success(data))
        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        }
    }
}