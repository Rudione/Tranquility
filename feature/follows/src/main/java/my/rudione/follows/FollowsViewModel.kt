package my.rudione.follows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.sampleUsers

class FollowsViewModel: ScreenModel {

    var uiState by mutableStateOf(FollowUiState())
        private set

    fun fetchFollows(userId: Long, followsType: Int) {
        screenModelScope.launch {

            uiState = uiState.copy(
                isLoading = true
            )

            delay(1000)

            uiState = uiState.copy(isLoading = false, followsUsers = sampleUsers)
        }
    }
}