package my.rudione.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.sampleComments
import my.rudione.common.fake_data.sampleSamplePosts

class PostDetailViewModel: ScreenModel {
    var postUiState by mutableStateOf(PostUiState())
        private set

    var commentsUiState by mutableStateOf(CommentsUiState())
        private set

    fun fetchData(postId: Long) {
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
                samplePost = sampleSamplePosts.find {
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