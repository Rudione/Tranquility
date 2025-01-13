package my.rudione.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.sampleSamplePosts
import my.rudione.common.fake_data.sampleProfiles
import my.rudione.common.util.Constants
import my.rudione.common.util.DefaultPagingManager
import my.rudione.common.util.Event
import my.rudione.common.util.EventBus
import my.rudione.common.util.PagingManager
import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.account.domain.usecase.GetProfileUseCase
import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.follows.domain.usecase.FollowOrUnfollowUseCase
import my.rudione.tranquility.post.domain.usecase.GetUserPostsUseCase
import my.rudione.tranquility.post.domain.usecase.LikeOrDislikePostUseCase

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val followOrUnfollowUseCase: FollowOrUnfollowUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val likesPostUseCase: LikeOrDislikePostUseCase
): ScreenModel {

    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set

    var profilePostsUiState by mutableStateOf(ProfilePostsUiState())
        private set

    private lateinit var pagingManager: PagingManager<Post>

    private fun fetchProfile(userId: Long) {
        screenModelScope.launch {
            getProfileUseCase(profileId = userId)
                .onEach {
                    when(it) {
                        is Result.Error -> {
                            userInfoUiState = userInfoUiState.copy(
                                isLoading = false,
                                errorMessage = it.message
                            )
                        }
                        is Result.Success -> {
                            userInfoUiState = userInfoUiState.copy(
                                isLoading = false,
                                profile = it.data
                            )
                            fetchProfilePosts(profileId = userId)
                        }
                    }
                }.collect()
        }
    }

    private suspend fun fetchProfilePosts(profileId: Long) {
        if (profilePostsUiState.isLoading || profilePostsUiState.posts.isNotEmpty()) return

        if (!::pagingManager.isInitialized) {
            pagingManager = createPagingManager(profileId = profileId)
        }

        pagingManager.loadItems()
    }

    private fun createPagingManager(profileId: Long): PagingManager<Post> {
        return DefaultPagingManager(
            onRequest = { page ->
                getUserPostsUseCase(
                    userId = profileId,
                    page = page,
                    pageSize = Constants.DEFAULT_REQUEST_PAGE_SIZE
                )
            },
            onSuccess = {posts, _ ->
                profilePostsUiState = if (posts.isEmpty()) {
                    profilePostsUiState.copy(
                        endReached = true
                    )
                } else {
                    profilePostsUiState.copy(
                        posts = profilePostsUiState.posts + posts,
                        endReached = posts.size < Constants.DEFAULT_REQUEST_PAGE_SIZE
                    )
                }
            },
            onError = {message, _ ->
                profilePostsUiState = profilePostsUiState.copy(
                    errorMessage = message
                )
            },
            onLoadStateChange = {isLoading ->
                profilePostsUiState = profilePostsUiState.copy(isLoading = isLoading)
            }
        )
    }

    private fun loadMorePosts() {
        if (profilePostsUiState.endReached) return
        screenModelScope.launch {
            pagingManager.loadItems()
        }
    }

    private fun followUser(profile: Profile) {
        screenModelScope.launch {
            val count = if (profile.isFollowing) -1 else +1
            userInfoUiState = userInfoUiState.copy(
                profile = userInfoUiState.profile?.copy(
                    isFollowing = !profile.isFollowing,
                    followersCount = profile.followersCount.plus(count)
                )
            )

            val result = followOrUnfollowUseCase(
                followedUserId = profile.id,
                shouldFollow = !profile.isFollowing
            )

            when(result) {
                is Result.Error -> {
                    userInfoUiState = userInfoUiState.copy(
                        profile = userInfoUiState.profile?.copy(
                            isFollowing = profile.isFollowing,
                            followersCount = profile.followersCount
                        )
                    )
                }
                is Result.Success -> Unit
            }
        }
    }

    private fun likeOrDislikePost(post: Post) {
        screenModelScope.launch {
            val count = if (post.isLiked) -1 else +1
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount.plus(count)
            )

            updatePost(updatedPost)

            val result = likesPostUseCase(
                post = post
            )

            when(result) {
                is Result.Error -> {
                    updatePost(post)
                }
                is Result.Success -> {
                    EventBus.send(Event.PostUpdated(updatedPost))
                }
            }
        }
    }

    private fun updatePost(post: Post) {
        profilePostsUiState = profilePostsUiState.copy(
            posts = profilePostsUiState.posts.map {
                if (it.postId == post.postId) post else it
            }
        )
    }

    fun onUiAction(uiAction: ProfileUiAction) {
        when(uiAction) {
            is ProfileUiAction.FetchProfileAction -> {
                fetchProfile(uiAction.profileId)
            }
            is ProfileUiAction.FollowUserAction -> {
                followUser(uiAction.profile)
            }
            is ProfileUiAction.LoadMorePostsAction -> {
                loadMorePosts()
            }
            is ProfileUiAction.PostLikeAction -> {
                likeOrDislikePost(uiAction.post)
            }
        }
    }
}

