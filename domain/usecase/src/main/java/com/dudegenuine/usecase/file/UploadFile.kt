package com.dudegenuine.usecase.file

import android.net.Uri
import com.dudegenuine.model.File
import com.dudegenuine.model.Resource
import com.dudegenuine.model.validation.HttpFailureException
import com.dudegenuine.repository.contract.IFileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Wed, 05 Jan 2022
 * WhoKnows by utifmd
 **/
class UploadFile
    @Inject constructor(
    val repository: IFileRepository) {

    operator fun invoke(uri: Uri): Flow<Resource<File>> = flow {
        try {
            emit(Resource.Loading())

            val uploaded = repository.upload(uri)

            emit(Resource.Success(uploaded))
        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(Resource.IO_EXCEPTION))
        }
    }
}