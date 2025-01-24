package my.rudione.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import my.rudione.common.util.Constants
import my.rudione.common.util.Constants.DEFAULT_REQUEST_PAGE_SIZE
import my.rudione.common.util.DefaultPagingManager
import my.rudione.common.util.Event
import my.rudione.common.util.EventBus
import my.rudione.common.util.PagingManager
import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.post.domain.model.PostComment
import my.rudione.tranquility.post.domain.usecase.AddPostCommentUseCase
import my.rudione.tranquility.post.domain.usecase.GetPostCommentsUseCase
import my.rudione.tranquility.post.domain.usecase.GetPostUseCase
import my.rudione.tranquility.post.domain.usecase.LikeOrDislikePostUseCase
import my.rudione.tranquility.post.domain.usecase.RemovePostCommentUseCase

class PostDetailViewModel(
    private val getPostUseCase: GetPostUseCase,
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val likeOrDislikePostUseCase: LikeOrDislikePostUseCase,
    private val addPostCommentsUseCase: AddPostCommentUseCase,
    private val removePostCommentUseCase: RemovePostCommentUseCase
): ScreenModel {
    var postUiState by mutableStateOf(PostUiState())
        private set

    var commentsUiState by mutableStateOf(CommentsUiState())
        private set

    private lateinit var pagingManager: PagingManager<PostComment>

    init {
        EventBus.events
            .onEach {
                when(it) {
                    is Event.PostUpdated -> updatePost(it.post)
                    is Event.ProfileUpdated -> updateCurrentUserProfileData(it.profile)
                }
            }
            .launchIn(screenModelScope)
    }

    private fun fetchData(postId: Long) {
        screenModelScope.launch {
            delay(500)

            when(val result = getPostUseCase(postId = postId)) {
                is Result.Success -> {
                    postUiState = postUiState.copy(
                        isLoading = false,
                        post = result.data
                    )
                    fetchPostComments(postId)
                }
                is Result.Error -> {
                    postUiState = postUiState.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private suspend fun fetchPostComments(postId: Long) {
        if (commentsUiState.isLoading || commentsUiState.comments.isNotEmpty()) {
            return
        }

        if (!::pagingManager.isInitialized) {
            pagingManager = createPagingManager(postId = postId)
        }

        pagingManager.loadItems()
    }

    private fun loadMoreComments() {
        if (commentsUiState.endReached) return
        screenModelScope.launch {
            pagingManager.loadItems()
        }
    }

    private fun createPagingManager(postId: Long): PagingManager<PostComment> {
        return DefaultPagingManager(
            onRequest = { page ->
                getPostCommentsUseCase(
                    postId = postId,
                    page = page,
                    pageSize = DEFAULT_REQUEST_PAGE_SIZE
                )
            },
            onSuccess = { comments, _ ->
                commentsUiState = commentsUiState.copy(
                    comments = commentsUiState.comments + comments,
                    endReached = comments.size < Constants.DEFAULT_REQUEST_COMMENTS_PAGE_SIZE
                )
            },
            onError = { message, _ ->
                commentsUiState = commentsUiState.copy(
                    errorMessage = message
                )
            },
            onLoadStateChange = { isLoading ->
                commentsUiState = commentsUiState.copy(
                    isLoading = isLoading
                )
            }
        )
    }

    private fun likeOrDislikePost(post: Post) {
        screenModelScope.launch {
            val count = if (post.isLiked) -1 else +1
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = post.likesCount.plus(count)
            )

            updatePost(updatedPost)

            val result = likeOrDislikePostUseCase(
                post = post,
            )

            when (result) {
                is Result.Error -> {
                    //if the operation fails, then rollback
                    updatePost(post)
                }

                is Result.Success -> {
                    EventBus.send(Event.PostUpdated(updatedPost))
                }
            }
        }
    }

    private fun updatePost(post: Post) {
        postUiState = postUiState.copy(
            post = post
        )
    }

    private fun addNewComment(comment: String) {
        screenModelScope.launch {
            val post = postUiState.post ?: return@launch

            commentsUiState = commentsUiState.copy(isAddingNewComment = true)
            delay(500)

            val result = addPostCommentsUseCase(
                postId = post.postId,
                content = comment
            )

            when (result) {
                is Result.Error -> {
                    commentsUiState = commentsUiState.copy(
                        errorMessage = result.message,
                        isAddingNewComment = false
                    )
                }

                is Result.Success -> {
                    val newComment = result.data ?: return@launch
                    val updatedComments = listOf(newComment) + commentsUiState.comments
                    commentsUiState = commentsUiState.copy(
                        comments = updatedComments,
                        isAddingNewComment = false
                    )


                    val updatedPost = post.copy(
                        commentsCount = post.commentsCount.plus(1)
                    )
                    EventBus.send(Event.PostUpdated(updatedPost))
                }

            }
        }
    }

    private fun removeComment(postComment: PostComment) {
        screenModelScope.launch {
            val post = postUiState.post ?: return@launch

            val comments = commentsUiState.comments
            val updatedComments = comments.filter { it.commentId != postComment.commentId }
            commentsUiState = commentsUiState.copy(comments = updatedComments)

            val result = removePostCommentUseCase(
                postId = post.postId,
                commentId = postComment.commentId
            )

            when (result) {
                is Result.Error -> {
                    commentsUiState = commentsUiState.copy(
                        errorMessage = result.message,
                        comments = comments
                    )
                }

                is Result.Success -> {
                    val updatedPost = post.copy(
                        commentsCount = post.commentsCount.minus(other = 1)
                    )
                    EventBus.send(Event.PostUpdated(updatedPost))
                }
            }
        }
    }

    private fun updateCurrentUserProfileData(profile: Profile) {
        val post = postUiState.post ?: return
        if (post.isOwnPost) {
            val updatedPost = post.copy(
                userName = profile.name,
                userImageUrl = profile.imageUrl
            )
            updatePost(updatedPost)
        }
        commentsUiState = commentsUiState.copy(
            comments = commentsUiState.comments.map {
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

    fun onUiAction(uiAction: PostDetailUiAction) {
        when(uiAction) {
            is PostDetailUiAction.FetchPostAction -> fetchData(uiAction.postId)
            is PostDetailUiAction.LoadMoreCommentsAction -> loadMoreComments()
            is PostDetailUiAction.LikeOrDislikePostAction -> likeOrDislikePost(uiAction.post)
            is PostDetailUiAction.AddCommentAction -> addNewComment(uiAction.comment)
            is PostDetailUiAction.RemoveCommentAction -> removeComment(uiAction.postComment)
        }
    }
}

sealed interface PostDetailUiAction {
    data class FetchPostAction(val postId: Long): PostDetailUiAction
    data object LoadMoreCommentsAction: PostDetailUiAction
    data class LikeOrDislikePostAction(val post: Post): PostDetailUiAction
    data class AddCommentAction(val comment: String): PostDetailUiAction
    data class RemoveCommentAction(val postComment: PostComment): PostDetailUiAction
}