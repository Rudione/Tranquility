package my.rudione.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.Comment
import my.rudione.common.fake_data.Post
import my.rudione.common.fake_data.sampleComments
import my.rudione.common.fake_data.samplePosts

class PostDetailViewModel: ScreenModel {
    var postUiState by mutableStateOf(PostUiState())
        private set

    var commentsUiState by mutableStateOf(CommentsUiState())
        private set

    fun fetchData(postId: String) {
        screenModelScope.launch {
            postUiState = postUiState.copy(
                isLoading = true
            )

            commentsUiState = commentsUiState.copy(
                isLoading = true
            )

            delay(500)

            postUiState = postUiState.copy(
                isLoading = false,
                post = samplePosts.find {
                    it.id == postId
                }
            )

            commentsUiState = commentsUiState.copy(
                isLoading = false,
                comments = sampleComments
            )
        }
    }
}

data class PostUiState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val errorMessage: String? = null
)

data class CommentsUiState(
    val isLoading: Boolean = false,
    val comments: List<Comment> = listOf(),
    val errorMessage: String? = null
)