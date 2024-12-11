package my.rudione.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import my.rudione.tranquility.auth.domain.common.Result
import my.rudione.tranquility.auth.domain.usecase.SignUpUseCase

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
): ScreenModel {
    private var _uiState by mutableStateOf(SignUpUiState())
    val uiState: SignUpUiState = _uiState

    suspend fun signUp() {
        _uiState = _uiState.copy(isAuthenticating = true)

        val authResultData = signUpUseCase(
            uiState.username,
            uiState.email,
            uiState.password
        )

        _uiState = when (authResultData) {
            is Result.Success -> _uiState.copy(
                isAuthenticating = false,
                isAuthSuccess = true
            )
            is Result.Error -> _uiState.copy(
                isAuthenticating = false,
                authErrorMessage = authResultData.errorMessage
            )
        }
    }

    fun updateUsername(username: String) {
        _uiState = uiState.copy(username = username)
    }

    fun updateEmail(email: String) {
        _uiState = uiState.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState = uiState.copy(password = password)
    }
}