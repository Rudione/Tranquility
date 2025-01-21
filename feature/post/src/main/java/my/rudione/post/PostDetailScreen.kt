package my.rudione.post

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.Comment
import my.rudione.common.fake_data.sampleComments
import my.rudione.common.fake_data.sampleSamplePosts
import my.rudione.common.util.Constants
import my.rudione.common.util.toCurrentUrl
import my.rudione.designsystem.TranquilityIcons
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.SmallSpacing
import my.rudione.tranquility.post.domain.model.PostComment
import my.rudione.ui.components.CircleImage
import my.rudione.ui.components.CommentListItem
import my.rudione.ui.components.PostListItem
import my.rudione.ui.components.ScreenLevelLoadingErrorView
import my.rudione.ui.components.ScreenLevelLoadingView
import my.rudione.ui.components.loadingMoreItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postUiState: PostUiState,
    commentsUiState: CommentsUiState,
    postId: Long,
    onProfileNavigation: (userId: Long) -> Unit,
    onUiAction: (PostDetailUiAction) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldFetchMoreComments by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount)
            }
        }
    }

    var commentText by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedComment by rememberSaveable(stateSaver = postCommentSaver) {
        mutableStateOf(null)
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { scope.launch { sheetState.hide() } },
    ) {
        selectedComment?.let { postComment ->
            CommentMoreActionsBottomSheetContent(
                comment = postComment,
                canDeleteComment = postComment.userId == postUiState.post?.userId || postComment.isOwner,
                onDeleteCommentClick = { comment ->
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onUiAction(PostDetailUiAction.RemoveCommentAction(comment))
                            selectedComment = null
                        }
                    }
                },
                onNavigateToProfile = { userId ->
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            selectedComment = null
                            onProfileNavigation(userId)
                        }
                    }
                }
            )
        }

        if (postUiState.isLoading) {
            ScreenLevelLoadingView()
        } else if (postUiState.post != null) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .weight(1f),
                    state = listState
                ) {
                    item(key = Constants.POST_ITEM_KEY) {
                        PostListItem(
                            post = postUiState.post,
                            onPostClick = {},
                            onProfileClick = {},
                            onLikeClick = { onUiAction(PostDetailUiAction.LikeOrDislikePostAction(it)) },
                            onCommentClick = {}
                        )
                    }

                    item(key = Constants.COMMENTS_HEADER_KEY) {
                        CommentsHeaderSection()
                    }

                    if (commentsUiState.isAddingNewComment) {
                        loadingMoreItem()
                    }

                    items(
                        items = commentsUiState.comments,
                        key = { comment -> comment.commentId }
                    ) {
                        HorizontalDivider()
                        CommentListItem(
                            comment = it,
                            onProfileClick = {},
                            onMoreIconClick = {
                                selectedComment = it
                                scope.launch { sheetState.show() }
                            }
                        )
                    }

                    if (commentsUiState.isLoading) {
                        loadingMoreItem()
                    }
                }

                CommentInput(
                    commentText = commentText,
                    onCommentChange = {
                        commentText = it
                    },
                    onSendClick = {
                        keyboardController?.hide()
                        onUiAction(PostDetailUiAction.AddCommentAction(it))
                        commentText = ""
                    }
                )
            }
        } else {
            ScreenLevelLoadingErrorView {
                onUiAction(PostDetailUiAction.FetchPostAction(postId))
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        onUiAction(PostDetailUiAction.FetchPostAction(postId))
    }

    LaunchedEffect(key1 = shouldFetchMoreComments) {
        if (shouldFetchMoreComments && !commentsUiState.endReached) {
            onUiAction(PostDetailUiAction.LoadMoreCommentsAction)
        }
    }
}

@Composable
private fun CommentInput(
    modifier: Modifier = Modifier,
    commentText: String,
    onCommentChange: (String) -> Unit,
    onSendClick: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .animateContentSize()
    ) {
        Divider()

        Row(
            modifier = modifier.padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LargeSpacing)
        ) {
            Box(
                modifier = modifier
                    .heightIn(min = 35.dp, max = 70.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(percent = 25)
                    )
                    .padding(
                        horizontal = MediumSpacing,
                        vertical = SmallSpacing
                    )
                    .weight(1f)
            ) {
                BasicTextField(
                    value = commentText,
                    onValueChange = onCommentChange,
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart),
                    textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
                    cursorBrush = SolidColor(LocalContentColor.current)
                )

                if (commentText.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = SmallSpacing),
                        text = stringResource(id = my.rudione.designsystem.R.string.comment_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }

            SendCommentButton(
                sendCommentEnabled = commentText.isNotBlank(),
                onSendClick = {
                    onSendClick(commentText)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentMoreActionsBottomSheetContent(
    modifier: Modifier = Modifier,
    comment: PostComment,
    canDeleteComment: Boolean,
    onDeleteCommentClick: (comment: PostComment) -> Unit,
    onNavigateToProfile: (userId: Long) -> Unit
) {
    Column {
        Text(
            text = stringResource(
                id = my.rudione.designsystem.R.string.comment_more_actions_title
            ),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(all = LargeSpacing)
        )

        Divider()

        ListItem(
            modifier = modifier.clickable(
                enabled = canDeleteComment,
                onClick = {
                    onDeleteCommentClick(comment)
                }
            ),
            headlineContent = {
                Text(
                    text = stringResource(id = my.rudione.designsystem.R.string.delete_comment_action),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Icon(TranquilityIcons.DELETE, contentDescription = null)
            }
        )

        ListItem(
            modifier = modifier.clickable {
                onNavigateToProfile(comment.userId)
            },
            headlineContent = {
                Text(
                    text = stringResource(
                        id = my.rudione.designsystem.R.string.view_profile_action,
                        comment.userName
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                CircleImage(
                    url = comment.userImageUrl?.toCurrentUrl(),
                    modifier = modifier
                        .size(25.dp),
                    onClick = {}
                )
            }
        )
    }
}

@Composable
private fun SendCommentButton(
    modifier: Modifier = Modifier,
    sendCommentEnabled: Boolean,
    onSendClick: () -> Unit
) {
    val border = if (!sendCommentEnabled) {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    } else {
        null
    }

    Button(
        modifier = modifier.height(35.dp),
        enabled = sendCommentEnabled,
        onClick = onSendClick,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        ),
        border = border,
        shape = RoundedCornerShape(percent = 50),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            stringResource(id = my.rudione.designsystem.R.string.send_button_text),
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }

}

@Composable
private fun CommentsHeaderSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = LargeSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = my.rudione.designsystem.R.string.comments_label),
            style = MaterialTheme.typography.titleSmall,
            modifier = modifier.weight(1f)
        )
    }
}

private val postCommentSaver = Saver<PostComment?, Any>(
    save = { postComment ->
        if (postComment != null) {
            mapOf(
                "commentId" to postComment.commentId,
                "content" to postComment.content,
                "postId" to postComment.postId,
                "userId" to postComment.userId,
                "userName" to postComment.userName,
                "userImageUrl" to postComment.userImageUrl,
                "createdAt" to postComment.createdAt
            )
        } else {
            null
        }
    },
    restore = { savedValue ->
        val map = savedValue as Map<*, *>
        PostComment(
            commentId = map["commentId"] as Long,
            content = map["content"] as String,
            postId = map["postId"] as Long,
            userId = map["userId"] as Long,
            userName = map["userName"] as String,
            userImageUrl = map["userImageUrl"] as String?,
            createdAt = map["createdAt"] as String
        )
    }
)

@Composable
@Preview
fun PostDetailScreenPreview() {
    PostDetailScreen(
        postUiState = PostUiState(
            post = sampleSamplePosts.first().toDomainPost(),
            isLoading = false
        ),
        commentsUiState = CommentsUiState(
            comments = sampleComments.map { it.toDomainComment() },
            isLoading = false
        ),
        onProfileNavigation = {},
        onUiAction = {},
        postId = 1
    )
}