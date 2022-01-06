package com.dudegenuine.usecase.file

import android.content.Context
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
    val context: Context,
    val repository: IFileRepository) {

    operator fun invoke(byteArray: ByteArray): Flow<Resource<File>> = flow {
        try {
            emit(Resource.Loading())

            /*uri.path?.let { path ->
                val actualImageFile = java.io.File(path)

                val file = Compressor.compress(context, actualImageFile) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.PNG)
                    size(597_152) //size(2_097_152) // 500KB
                }
            }


            val mUri = Uri.fromFile(file)*/

            val uploaded = repository.upload(byteArray)

            emit(Resource.Success(uploaded))
        } catch (e: HttpFailureException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_FAILURE_EXCEPTION))
        } catch (e: IOException){
            emit(Resource.Error(e.localizedMessage ?: Resource.IO_EXCEPTION))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: Resource.HTTP_EXCEPTION))
        }
    }
}