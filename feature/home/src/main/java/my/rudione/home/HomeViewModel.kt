package my.rudione.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.home.onboarding.OnBoardingUiState
import my.rudione.tranquility.common.domain.model.FollowsUser
import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.follows.domain.usecase.FollowOrUnfollowUseCase
import my.rudione.tranquility.follows.domain.usecase.GetFollowableUsersUseCase

class HomeViewModel(
    private val getFollowableUsersUseCase: GetFollowableUsersUseCase,
    private val followOrUnfollowUseCase: FollowOrUnfollowUseCase
) : ScreenModel {
    var postsFeedUiState by mutableStateOf(PostsFeedUiState())
        private set

    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set

    var homeRefreshState by mutableStateOf(HomeRefreshState())
        private set

    init {
        fetchData()
    }

    fun fetchData() {
        homeRefreshState = homeRefreshState.copy(isRefreshing = true)

        screenModelScope.launch {
            delay(1000)

            val users = getFollowableUsersUseCase()
            handleOnBoardingResult(users)
            postsFeedUiState = postsFeedUiState.copy(
                isLoading = false,
                post = postsFeedUiState.post,
                errorMessage = null
            )
            homeRefreshState = homeRefreshState.copy(isRefreshing = false)

        }
    }

    private fun handleOnBoardingResult(result: Result<List<FollowsUser>>) {
        when (result) {
            is Result.Error -> Unit

            is Result.Success -> {
                result.data?.let { followsUsers ->
                    onBoardingUiState = onBoardingUiState.copy(
                        shouldShowOnBoarding = followsUsers.isNotEmpty(),
                        users = followsUsers
                    )
                }
            }
        }
    }

    private fun followUser(followsUser: FollowsUser) {
        screenModelScope.launch {
            val result = followOrUnfollowUseCase(
                followedUserId = followsUser.id,
                shouldFollow = !followsUser.isFollowing
            )

            onBoardingUiState = onBoardingUiState.copy(
                users = onBoardingUiState.users.map {
                    if (it.id == followsUser.id) {
                        it.copy(isFollowing = !followsUser.isFollowing)
                    } else it
                }
            )

            when (result) {
                is Result.Error -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        users = onBoardingUiState.users.map {
                            if (it.id == followsUser.id) {
                                it.copy(isFollowing = followsUser.isFollowing)
                            } else it
                        }
                    )
                }

                is Result.Success -> Unit
            }
        }
    }

    private fun dismissOnboarding() {
        val hasFollowing = onBoardingUiState.users.any { it.isFollowing }
        if (!hasFollowing) {
            //TODO tell the user they need to follow at least one person
        } else {
            onBoardingUiState =
                onBoardingUiState.copy(shouldShowOnBoarding = false, users = emptyList())
            fetchData()
        }
    }

    fun onUiAction(uiAction: HomeUiAction) {
        when (uiAction) {
            is HomeUiAction.FollowUserAction -> followUser(uiAction.user)
            HomeUiAction.LoadMorePostsAction -> Unit
            is HomeUiAction.PostLikeAction -> Unit
            HomeUiAction.RefreshAction -> fetchData()
            HomeUiAction.RemoveOnboardingAction -> dismissOnboarding()
        }
    }
}

data class PostsFeedUiState(
    val isLoading: Boolean = false,
    val post: List<Post> = listOf(),
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