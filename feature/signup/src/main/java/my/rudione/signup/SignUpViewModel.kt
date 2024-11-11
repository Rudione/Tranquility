package my.rudione.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel

class SignUpViewModel: ScreenModel {
    private var _uiState by mutableStateOf(SignUpUiState())
    val uiState: SignUpUiState = _uiState

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