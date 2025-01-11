package my.rudione.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import my.rudione.common.fake_data.Comment
import my.rudione.common.fake_data.sampleComments
import my.rudione.common.fake_data.sampleSamplePosts
import my.rudione.common.util.Constants
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.ui.components.CommentListItem
import my.rudione.ui.components.PostListItem

@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postUiState: PostUiState,
    commentsUiState: CommentsUiState,
    onCommentMoreIconClick: (Comment) -> Unit,
    onProfileClick: (Long) -> Unit,
    onAddCommentClick: () -> Unit,
    fetchData: () -> Unit
) {
    if (postUiState.isLoading && commentsUiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (postUiState.samplePost != null) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            item(key = Constants.POST_ITEM_KEY) {
                PostListItem(
                    post = postUiState.samplePost.toDomainPost(),
                    onPostClick = {},
                    onProfileClick = onProfileClick,
                    onLikeClick = {},
                    onCommentClick = {}
                )
            }

            item(key = Constants.COMMENTS_HEADER_KEY) {
                CommentsSectionHeader {
                    onAddCommentClick()
                }
            }

            items(
                items = sampleComments,
                key = { comment -> comment.id }
            ) {
                HorizontalDivider()
                CommentListItem(
                    comment = it, onProfileClick = onProfileClick
                ) {
                    onCommentMoreIconClick(it)
                }
            }
        }
    } else {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(my.rudione.designsystem.R.string.loading_error_message),
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedButton(
                    onClick = {
                        fetchData()
                    }
                ) {
                    Text(
                        text = stringResource(id = my.rudione.designsystem.R.string.retry_button_text)
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        fetchData()
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
            .padding(LargeSpacing),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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

@Composable
@Preview
fun PostDetailScreenPreview() {
    PostDetailScreen(
        postUiState = PostUiState(
            samplePost = sampleSamplePosts.first(),
            isLoading = false
        ),
        commentsUiState = CommentsUiState(
            comments = sampleComments,
            isLoading = false
        ),
        onCommentMoreIconClick = {},
        onProfileClick = {},
        onAddCommentClick = {},
        fetchData = {}
    )
}