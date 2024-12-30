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
import my.rudione.tranquility.auth.domain.usecase.SignInUseCase
import my.rudione.tranquility.common.util.Result

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val dataStore: DataStore<UserSettings>
): ScreenModel {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun signIn(){
        screenModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)

            val authResultData = signInUseCase(uiState.email, uiState.password)

            uiState = when(authResultData){
                is Result.Error -> {
                    uiState.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.message
                    )
                }
                is Result.Success -> {
                    dataStore.updateData {
                        authResultData.data!!.toUserSettings()
                    }
                    uiState.copy(
                        isAuthenticating = false,
                        authenticationSucceed = true
                    )
                }
                else -> {
                    uiState.copy(
                        isAuthenticating = false,
                        authErrorMessage = "Oops, something went wrong!"
                    )
                }
            }
        }
    }

    fun updateEmail(input: String){
        uiState = uiState.copy(email = input)
    }

    fun updatePassword(input: String){
        uiState = uiState.copy(password = input)
    }
}