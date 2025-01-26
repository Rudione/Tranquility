package my.rudione.createpost

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import my.rudione.common.util.Event
import my.rudione.common.util.EventBus
import my.rudione.common.util.ImageBytesReader
import my.rudione.tranquility.post.domain.usecase.CreatePostUseCase
import my.rudione.tranquility.common.util.Result

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val imageBytesReader: ImageBytesReader
): ScreenModel {
    var uiState by mutableStateOf(CreatePostUiState())
        private set

    private suspend fun uploadPost(imageBytes: ByteArray){
        val result = createPostUseCase(caption = uiState.caption, imageBytes = imageBytes)

        uiState = when(result){
            is Result.Error -> {
                uiState.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }
            is Result.Success -> {
                EventBus.send(event = Event.PostCreated(post = result.data!!))
                uiState.copy(
                    isLoading = false,
                    postCreated = true
                )
            }
        }
        Log.d("CreatePostViewModel", "uploadPost: $result, ${uiState.errorMessage}, ${uiState.caption}, ${uiState.postCreated}")
    }

    private fun readImageBytes(imageUri: Uri){
        uiState = uiState.copy(
            isLoading = true
        )

        screenModelScope.launch {
            when(val result = imageBytesReader.readImagesBytes(uri = imageUri)){
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    uploadPost(result.data!!)
                }
            }
        }

        Log.d("CreatePostViewModel", "readImageBytes: ${uiState.isLoading}, ${uiState.errorMessage}")
    }

    private fun onCaptionChange(input: String){
        uiState = uiState.copy(caption = input)
    }

    fun onUiAction(action: CreatePostUiAction){
        when(action){
            is CreatePostUiAction.CaptionChanged -> {
                onCaptionChange(input = action.input)
            }
            is CreatePostUiAction.CreatePostAction -> {
                readImageBytes(action.imageUri)
            }
        }
    }
}

sealed interface CreatePostUiAction{
    data class CaptionChanged(val input: String): CreatePostUiAction
    class CreatePostAction(val imageUri: Uri): CreatePostUiAction
}