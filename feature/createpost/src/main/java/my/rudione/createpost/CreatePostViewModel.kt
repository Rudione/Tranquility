package my.rudione.createpost

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import my.rudione.common.util.Event
import my.rudione.common.util.EventBus
import my.rudione.common.util.ImageBytesReader
import my.rudione.tranquility.post.domain.usecase.CreatePostUseCase
import my.rudione.tranquility.common.util.Result

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val imageBytesReader: ImageBytesReader,
    private val generativeModel: GenerativeModel
): ScreenModel {
    var uiState by mutableStateOf(CreatePostUiState())
        private set

    private var generativeState by mutableStateOf(
        GenerativePostUiState(isLoading = false, errorMessage = null)
    )
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

    private fun onCaptionChange(input: String) {
        Log.d("CreatePostVM", "Caption changed to: $input")
        uiState = uiState.copy(caption = input)
    }

    fun generateText(inputText: String) {
        generativeState = GenerativePostUiState(isLoading = true, errorMessage = null)

        val prompt = "Напиши за inputText'ом дуже короткий текст для блогу, де максимум написаних символів менше 120. Повторювати запит не треба. Відповідай швидко. $inputText"

        screenModelScope.launch {
            try {
                val response = generativeModel.generateContent(prompt)
                response.text?.let { outputContent ->
                    generativeState = GenerativePostUiState(isLoading = false, errorMessage = null, caption = outputContent)
                    uiState = uiState.copy(caption = outputContent)
                    Log.d("GenerativeViewModel", "Generated content: $outputContent")
                }
            } catch (e: Exception) {
                generativeState = GenerativePostUiState(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun onUiAction(action: CreatePostUiAction){
        when(action){
            is CreatePostUiAction.CaptionChanged -> {
                onCaptionChange(input = action.input)
            }
            is CreatePostUiAction.CreatePostAction -> {
                readImageBytes(action.imageUri)
            }
            is CreatePostUiAction.CaptionChangedWithAI -> {
                generateText(inputText = action.input)
            }
        }
    }
}

sealed interface CreatePostUiAction{
    data class CaptionChanged(val input: String): CreatePostUiAction
    data class CaptionChangedWithAI(val input: String): CreatePostUiAction
    class CreatePostAction(val imageUri: Uri): CreatePostUiAction
}

data class GenerativePostUiState(
    val isLoading: Boolean,
    val errorMessage: String?,
    val caption: String = ""
)