package my.rudione.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class LoginViewModel: ScreenModel {
    private var _uiState by mutableStateOf(LoginUiState())
    val uiState: LoginUiState = _uiState

    fun updateEmail(email: String) {
        _uiState = uiState.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState = uiState.copy(password = password)
    }
}