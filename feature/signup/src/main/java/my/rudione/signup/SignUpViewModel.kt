package my.rudione.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import my.rudione.tranquility.auth.domain.usecase.SignUpUseCase
import my.rudione.tranquility.common.util.Result

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
): ScreenModel {
    var uiState by mutableStateOf(SignUpUiState())
        private set

    fun signUp(){
        screenModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)

            val authResultData = signUpUseCase(
                name = uiState.username,
                email = uiState.email,
                password = uiState.password
            )
            Log.d("SignUpViewModel", "signUp: ${authResultData.message}")

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
            Log.d("SignUpViewModel", "signUp: ${authResultData.message}, ${uiState.authErrorMessage}")
        }
    }

    fun updateUsername(input: String){
        uiState = uiState.copy(username = input)
        Log.d("UpdateUsername", "updateUsername: $uiState")
    }

    fun updateEmail(input: String){
        uiState = uiState.copy(email = input)
        Log.d("UpdateEmail", "updateEmail: $uiState")
    }

    fun updatePassword(input: String){
        uiState = uiState.copy(password = input)
        Log.d("UpdatePassword", "updatePassword: $uiState")
    }
}