package my.rudione.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import my.rudione.tranquility.auth.domain.usecase.SignUpUseCase
import my.rudione.tranquility.common.util.Result

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
): ScreenModel {
    var uiState by mutableStateOf(SignUpUiState())
        private set

    fun signUp(){
        screenModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)

            val authResultData = signUpUseCase(uiState.email, uiState.username, uiState.password)

            uiState = when(authResultData){
                is Result.Error -> {
                    uiState.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.message
                    )
                }
                is Result.Success -> {
                    uiState.copy(
                        isAuthenticating = false,
                        isAuthSuccess = true
                    )
                }
            }
        }
    }

    fun updateUsername(input: String){
        uiState = uiState.copy(username = input)
    }

    fun updateEmail(input: String){
        uiState = uiState.copy(email = input)
    }

    fun updatePassword(input: String){
        uiState = uiState.copy(password = input)
    }
}