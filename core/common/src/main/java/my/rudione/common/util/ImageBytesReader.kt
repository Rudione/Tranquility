package my.rudione.common.util

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.rudione.tranquility.common.util.Result
import okio.IOException
import okio.use

class ImageBytesReader(
    private val appContext: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun readImagesBytes(uri: Uri): Result<ByteArray> {
        return withContext(ioDispatcher) {
            try {
                val bytes = appContext.contentResolver.openInputStream(uri)?.use {
                    it.readBytes()
                }
                bytes?.let {
                    Result.Success(it)
                } ?: Result.Error(message = Constants.READING_IMAGE_BYTES_FAILURE_MESSAGE)
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (ioException: IOException) {
                Result.Error(message = ioException.message ?: Constants.UNEXPECTED_ERROR_MESSAGE)
            } catch (exception: Exception) {
                Result.Error(message = exception.message ?: Constants.UNEXPECTED_ERROR_MESSAGE)
            }
        }
    }
}