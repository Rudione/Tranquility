package my.rudione.post

import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.post.domain.model.PostComment

data class PostUiState(
    val isLoading: Boolean = true,
    val post: Post? = null,
    val errorMessage: String? = null
)

data class CommentsUiState(
    val isLoading: Boolean = false,
    val comments: List<PostComment> = listOf(),
    val errorMessage: String? = null,
    val endReached: Boolean = false,
    val isAddingNewComment: Boolean = false
)