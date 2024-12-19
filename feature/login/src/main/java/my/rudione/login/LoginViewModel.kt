package my.rudione.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import my.rudione.common.datastore.UserSettings
import my.rudione.common.datastore.toUserSettings
import my.rudione.tranquility.auth.domain.common.Result
import my.rudione.tranquility.auth.domain.usecase.SignInUseCase

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val dataStore: DataStore<UserSettings>
): ScreenModel {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun signIn() {
        screenModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)

            val authResultData = signInUseCase(uiState.email, uiState.password)

            uiState = when (authResultData) {
                is Result.Error -> {
                    uiState.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.errorMessage
                    )
                }

                is Result.Success -> {
                    dataStore.updateData {
                        authResultData.data.toUserSettings()
                    }
                    uiState.copy(
                        isAuthenticating = false,
                        isAuthSuccess = true
                    )
                }
            }
        }
    }

    fun updateEmail(input: String) {
        uiState = uiState.copy(email = input)
    }

    fun updatePassword(input: String) {
        uiState = uiState.copy(password = input)
    }
}