package my.rudione.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import my.rudione.tranquility.auth.domain.common.Result
import my.rudione.tranquility.auth.domain.usecase.SignInUseCase

class LoginViewModel(
    private val signInUseCase: SignInUseCase
): ScreenModel {
    private var _uiState by mutableStateOf(LoginUiState())
    val uiState: LoginUiState = _uiState

    suspend fun signIn() {
        _uiState = uiState.copy(isAuthenticating = true)

        val authResultData = signInUseCase(_uiState.email, _uiState.password)

        _uiState = when(authResultData) {
            is Result.Success -> uiState.copy(
                isAuthenticating = false,
                isAuthSuccess = true
            )
            is Result.Error -> uiState.copy(
                isAuthenticating = false,
                authErrorMessage = authResultData.errorMessage
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState = uiState.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState = uiState.copy(password = password)
    }
}