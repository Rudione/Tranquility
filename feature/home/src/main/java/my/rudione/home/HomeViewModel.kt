package my.rudione.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.FollowsUser
import my.rudione.common.fake_data.Post
import my.rudione.common.fake_data.samplePosts
import my.rudione.common.fake_data.sampleUsers
import my.rudione.home.onboarding.OnBoardingUiState

class HomeViewModel : ScreenModel {
    var postsUiState by mutableStateOf(PostsUiState())
        private set

    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set

    init {
        fetchData()
    }

    fun fetchData() {
        onBoardingUiState = onBoardingUiState.copy(isLoading = true)
        postsUiState = postsUiState.copy(isLoading = true)

        screenModelScope.launch {
            delay(1000)

            onBoardingUiState = onBoardingUiState.copy(
                isLoading = false,
                users = sampleUsers,
                errorMessage = null,
                shouldShowOnBoarding = true
            )

            postsUiState = postsUiState.copy(
                isLoading = false,
                posts = samplePosts,
                errorMessage = null
            )
        }
    }
}

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false
)

data class HomeRefreshState(
    val isRefreshing: Boolean = false,
    val refreshErrorMessage: String? = null
)

sealed interface HomeUiAction {
    data class FollowUserAction(val user: FollowsUser) : HomeUiAction
    data class PostLikeAction(val post: Post) : HomeUiAction
    data object RemoveOnboardingAction : HomeUiAction
    data object RefreshAction : HomeUiAction
    data object LoadMorePostsAction : HomeUiAction
}