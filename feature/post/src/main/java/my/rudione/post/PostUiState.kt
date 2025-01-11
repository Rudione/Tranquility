package my.rudione.post

import my.rudione.common.fake_data.Comment
import my.rudione.common.fake_data.SamplePost

data class PostUiState(
    val isLoading: Boolean = false,
    val samplePost: SamplePost? = null,
    val errorMessage: String? = null
)

data class CommentsUiState(
    val isLoading: Boolean = false,
    val comments: List<Comment> = listOf(),
    val errorMessage: String? = null
)