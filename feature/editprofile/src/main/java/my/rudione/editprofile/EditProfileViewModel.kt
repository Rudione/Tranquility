package my.rudione.editprofile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import my.rudione.common.util.Event
import my.rudione.common.util.EventBus
import my.rudione.common.util.ImageBytesReader
import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.account.domain.usecase.GetProfileUseCase
import my.rudione.tranquility.account.domain.usecase.UpdateProfileUseCase
import my.rudione.tranquility.common.util.Result

class EditProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val imagesBytesReader: ImageBytesReader
): ScreenModel {

    var uiState: EditProfileUiState by mutableStateOf(EditProfileUiState())
        private set

    var bioTextFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(text = ""))
        private set

    private fun fetchProfile(userId: Long){
        screenModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            delay(1000)

            when (val result = getProfileUseCase(profileId = userId).first()) {
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }


                is Result.Success -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        profile = result.data
                    )
                }
            }

            bioTextFieldValue = bioTextFieldValue.copy(
                text = uiState.profile?.bio ?: "",
                selection = TextRange(index = uiState.profile?.bio?.length ?: 0)
            )
        }
    }

    private suspend fun uploadProfile(imageBytes: ByteArray?, profile: Profile){
        delay(1000)

        val result = updateProfileUseCase(
            profile = profile.copy(bio = bioTextFieldValue.text),
            imageBytes = imageBytes
        )

        when (result) {
            is Result.Error -> {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
            }


            is Result.Success -> {
                EventBus.send(Event.ProfileUpdated(result.data!!))
                uiState = uiState.copy(
                    isLoading = false,
                    uploadSucceed = true
                )
            }
        }
    }

    private fun readImageBytes(imageUri: Uri){
        val profile = uiState.profile ?: return

        uiState = uiState.copy(
            isLoading = true
        )

        screenModelScope.launch {
            if (imageUri == Uri.EMPTY){
                uploadProfile(imageBytes = null, profile = profile)
                return@launch
            }
            val result = imagesBytesReader.readImagesBytes(imageUri)
            when(result){
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    uploadProfile(imageBytes = result.data!!, profile = profile)
                }
            }
        }
    }

    fun onNameChange(inputName: String){
        uiState = uiState.copy(
            profile = uiState.profile?.copy(name = inputName)
        )
    }

    fun onBioChange(inputBio: TextFieldValue){
        bioTextFieldValue = bioTextFieldValue.copy(
            text = inputBio.text,
            selection = TextRange(index = inputBio.text.length)
        )
    }

    fun onUiAction(uiAction: EditProfileUiAction) {
        when (uiAction) {
            is EditProfileUiAction.FetchProfileAction -> fetchProfile(uiAction.userId)
            is EditProfileUiAction.UploadProfileAction -> readImageBytes(imageUri = uiAction.imageUri)
            else -> {}
        }
    }

}

sealed interface EditProfileUiAction{
    data class FetchProfileAction(val userId: Long): EditProfileUiAction
    class UploadProfileAction(val imageUri: Uri = Uri.EMPTY) : EditProfileUiAction
}