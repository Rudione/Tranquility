package my.rudione.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.sampleSamplePosts
import my.rudione.common.fake_data.sampleProfiles

class ProfileViewModel: ScreenModel {

    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set

    var profilePostsUiState by mutableStateOf(ProfilePostsUiState())
        private set

    fun fetchProfile(userId: Long) {
        screenModelScope.launch {
            delay(1000)

            userInfoUiState = userInfoUiState.copy(
                isLoading = false,
                profile = sampleProfiles.find { it.id == userId }
            )

            profilePostsUiState = profilePostsUiState.copy(
                isLoading = false,
                samplePosts = sampleSamplePosts
            )
        }
    }
}