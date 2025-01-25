package my.rudione.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import my.rudione.common.util.Constants
import my.rudione.common.util.DefaultPagingManager
import my.rudione.common.util.Event
import my.rudione.common.util.EventBus
import my.rudione.common.util.PagingManager
import my.rudione.home.onboarding.OnBoardingUiState
import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.common.domain.model.FollowsUser
import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.follows.domain.usecase.FollowOrUnfollowUseCase
import my.rudione.tranquility.follows.domain.usecase.GetFollowableUsersUseCase
import my.rudione.tranquility.post.domain.usecase.GetPostsUseCase
import my.rudione.tranquility.post.domain.usecase.LikeOrDislikePostUseCase

class HomeViewModel(
    private val getFollowableUsersUseCase: GetFollowableUsersUseCase,
    private val followOrUnfollowUseCase: FollowOrUnfollowUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikeOrDislikePostUseCase
) : ScreenModel {
    var postsFeedUiState by mutableStateOf(PostsFeedUiState())
        private set

    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set

    var homeRefreshState by mutableStateOf(HomeRefreshState())
        private set

    private val pagingManager by lazy { createPagingManager() }

    init {
        fetchData()

        EventBus.events
            .onEach {
                when(it) {
                    is Event.PostUpdated -> updatePost(it.post)
                    is Event.ProfileUpdated -> updateCurrentUserPostsProfileData(it.profile)
                    is Event.PostCreated -> insertNewPost(it.post)
                }
            }.launchIn(screenModelScope)
    }

    private fun fetchData() {
        homeRefreshState = homeRefreshState.copy(isRefreshing = true)

        screenModelScope.launch {
            delay(1000)

            val onboardingDeferred = async {
                getFollowableUsersUseCase()
            }

            pagingManager.apply {
                reset()
                loadItems()
            }
            handleOnBoardingResult(onboardingDeferred.await())
            homeRefreshState = homeRefreshState.copy(isRefreshing = false)
        }
    }

    private fun createPagingManager(): PagingManager<Post> {
        return DefaultPagingManager(
            onRequest = { page ->
                getPostsUseCase(page, Constants.DEFAULT_REQUEST_PAGE_SIZE)
            },
            onSuccess = { posts, page ->
                postsFeedUiState = if (posts.isEmpty()) {
                    postsFeedUiState.copy(endReached = true)
                } else {
                    if (page == Constants.INITIAL_PAGE_NUMBER) {
                        postsFeedUiState = postsFeedUiState.copy(post = emptyList())
                    }
                    postsFeedUiState.copy(
                        post = postsFeedUiState.post + posts,
                        endReached = posts.size < Constants.DEFAULT_REQUEST_PAGE_SIZE
                    )
                }
            },
            onError = { cause, page ->
                if (page == Constants.INITIAL_PAGE_NUMBER) {
                    homeRefreshState = homeRefreshState.copy(
                        refreshErrorMessage = cause
                    )
                } else {
                    postsFeedUiState = postsFeedUiState.copy(
                        errorMessage = cause
                    )
                }
            },
            onLoadStateChange = { isLoading ->
                postsFeedUiState = postsFeedUiState.copy(
                    isLoading = isLoading
                )
            }
        )
    }

    private fun loadMorePosts() {
        if (postsFeedUiState.endReached) return
        screenModelScope.launch {
            pagingManager.loadItems()
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

    private fun likeOrUnlikePost(post: Post) {
        screenModelScope.launch {
            val count = if (post.isLiked) -1 else +1
            postsFeedUiState = postsFeedUiState.copy(
                post = postsFeedUiState.post.map {
                    if (it.postId == post.postId) {
                        it.copy(
                            isLiked = !post.isLiked,
                            likesCount = post.likesCount.plus(count)
                        )
                    } else it
                }
            )

            val result = likePostUseCase(post = post)

            when (result) {
                is Result.Error -> {
                    updatePost(post)
                }

                is Result.Success -> Unit
            }
        }
    }

    private fun insertNewPost(post: Post){
        postsFeedUiState = postsFeedUiState.copy(
            post = listOf(post) + postsFeedUiState.post
        )
    }

    private fun updatePost(post: Post) {
        postsFeedUiState = postsFeedUiState.copy(
            post = postsFeedUiState.post.map {
                if (it.postId == post.postId) post else it
            }
        )
    }

    private fun updateCurrentUserPostsProfileData(profile: Profile) {
        postsFeedUiState = postsFeedUiState.copy(
            post = postsFeedUiState.post.map {
                if (it.userId == profile.id) {//should use it.isOwnComment
                    it.copy(
                        userName = profile.name,
                        userImageUrl = profile.imageUrl
                    )
                } else {
                    it
                }
            }
        )
    }

    fun onUiAction(uiAction: HomeUiAction) {
        when (uiAction) {
            is HomeUiAction.FollowUserAction -> followUser(uiAction.user)
            HomeUiAction.LoadMorePostsAction -> loadMorePosts()
            is HomeUiAction.PostLikeAction -> likeOrUnlikePost(uiAction.post)
            HomeUiAction.RefreshAction -> fetchData()
            HomeUiAction.RemoveOnboardingAction -> dismissOnboarding()
        }
    }
}

sealed interface HomeUiAction {
    data class FollowUserAction(val user: FollowsUser) : HomeUiAction
    data class PostLikeAction(val post: Post) : HomeUiAction
    data object RemoveOnboardingAction : HomeUiAction
    data object RefreshAction : HomeUiAction
    data object LoadMorePostsAction : HomeUiAction
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