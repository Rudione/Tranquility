package my.rudione.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import my.rudione.common.fake_data.Comment
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.ui.components.PostListItem

@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postUiState: PostUiState,
    commentsUiState: CommentsUiState,
    onCommentMoreIconClick: (Comment) -> Unit,
    onProfileClick: (Long) -> Unit,
    onAddCommentClick: () -> Unit
) {
    if (postUiState.isLoading && commentsUiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (postUiState.post != null) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item(key = "post_item") {
                PostListItem(
                    post = postUiState.post,
                    onPostClick = {},
                    onProfileClick = onProfileClick,
                    onLikeClick = {},
                    onCommentClick = {}
                )
            }

            item(key = "comments_header_section") {
                CommentsSectionHeader {
                    onAddCommentClick()
                }
            }


        }
    }
}

@Composable
fun CommentsSectionHeader(
    modifier: Modifier = Modifier,
    onAddCommentClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LargeSpacing)
    ) {
        Text(
            text = stringResource(R.string.comments_header_label),
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedButton(
            onClick = onAddCommentClick
        ) {
            Text(text = stringResource(R.string.add_comment_outlined_button))
        }
    }
}