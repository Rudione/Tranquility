package my.rudione.editprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.sampleProfiles

class EditProfileViewModel: ScreenModel {

    var uiState: EditProfileUiState by mutableStateOf(EditProfileUiState())
        private set

    var bioTextFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(text = ""))
        private set

    fun fetchProfile(userId: Long) {
        screenModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )

            delay(1000)

            uiState = uiState.copy(
                isLoading = false,
                profile = sampleProfiles.find { it.id == userId }
            )

            bioTextFieldValue = bioTextFieldValue.copy(
                text = uiState.profile?.bio ?: "",
                selection = TextRange(index = uiState.profile?.bio?.length ?: 0)
            )
        }
    }

    fun uploadProfile() {
        screenModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )

            delay(1000)

            uiState = uiState.copy(
                isLoading = false,
                uploadSucceed = true
            )
        }
    }

    fun onNameChange(inputName: String) {
        uiState = uiState.copy(
            profile = uiState.profile?.copy(name = inputName)
        )
    }

    fun onBioChange(inputBio: TextFieldValue) {
        bioTextFieldValue = bioTextFieldValue.copy(
            text = inputBio.text,
            selection = TextRange(index = inputBio.text.length)
        )
    }
}