package my.rudione.profile

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.rudione.common.util.Constants
import my.rudione.common.util.toCurrentUrl
import my.rudione.designsystem.theme.ExtraLargeUnderAppBar
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.SmallSpacing
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.tranquility.common.domain.model.Post
import my.rudione.ui.components.CircleImage
import my.rudione.ui.components.FollowsButton
import my.rudione.ui.components.PostListItem

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: UserInfoUiState,
    profilePostsUiState: ProfilePostsUiState,
    profileId: Long,
    onUiAction: (ProfileUiAction) -> Unit,
    onFollowButtonClick: () -> Unit,
    onFollowersScreenNavigation: () -> Unit,
    onFollowingScreenNavigation: () -> Unit,
    onPostDetailNavigation: (Post) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldFetchMorePosts by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (layoutInfo.totalItemsCount == 0) {
                false
            }else{
                val lastVisibleItem = visibleItemsInfo.last()
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount)
            }
        }
    }

    if (userInfoUiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState
        ) {
            item(key = "header_section") {
                ProfileHeaderSection(
                    imageUrl = userInfoUiState.profile?.imageUrl ?: "",
                    name = userInfoUiState.profile?.name ?: "",
                    bio = userInfoUiState.profile?.bio ?: "",
                    followersCount = userInfoUiState.profile?.followersCount ?: 0,
                    followingCount = userInfoUiState.profile?.followingCount ?: 0,
                    isFollowing = userInfoUiState.profile?.isFollowing ?: false,
                    isCurrentUser = userInfoUiState.profile?.isOwnProfile ?: false,
                    onButtonClick = onFollowButtonClick,
                    onFollowersClick = onFollowersScreenNavigation,
                    onFollowingClick = onFollowingScreenNavigation
                )
            }

            items(
                items = profilePostsUiState.posts,
                key = { post -> post.postId }
            ) {
                PostListItem(
                    post = it,
                    onPostClick = {
                        onPostDetailNavigation(it)
                    },
                    onProfileClick = {},
                    onLikeClick = { post ->
                        onUiAction(ProfileUiAction.PostLikeAction(post))
                    },
                    onCommentClick = {}
                )
            }

            if (profilePostsUiState.isLoading) {
                item(key = Constants.LOADING_MORE_ITEM_KEY) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = MediumSpacing,
                                horizontal = LargeSpacing
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        onUiAction(ProfileUiAction.FetchProfileAction(profileId = profileId))
    }

    LaunchedEffect(key1 = shouldFetchMorePosts) {
        if (shouldFetchMorePosts && !profilePostsUiState.endReached) {
            onUiAction(ProfileUiAction.LoadMorePostsAction)
        }
    }
}

@Composable
fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    name: String,
    bio: String,
    followersCount: Int,
    followingCount: Int,
    isCurrentUser: Boolean = false,
    isFollowing: Boolean = false,
    onButtonClick: () -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = MediumSpacing)
            .padding(top = ExtraLargeUnderAppBar)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(all = LargeSpacing)
    ) {
        CircleImage(
            url = imageUrl?.toCurrentUrl(),
            modifier = Modifier.padding(bottom = MediumSpacing).size(90.dp),
            onClick = {}
        )

        Spacer(modifier = modifier.height(SmallSpacing))

        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = bio,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = modifier.height(MediumSpacing))

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = modifier.weight(1f)
            ) {
                FollowsText(
                    count = followersCount,
                    text = my.rudione.designsystem.R.string.followers_text,
                    onClick = onFollowersClick
                )
                
                Spacer(modifier = modifier.width(MediumSpacing))

                FollowsText(
                    count = followingCount,
                    text = my.rudione.designsystem.R.string.following_text,
                    onClick = onFollowingClick
                )
            }

            FollowsButton(
                text = when {
                    isFollowing -> my.rudione.designsystem.R.string.unfollow_text_label
                    isCurrentUser -> my.rudione.designsystem.R.string.edit_profile_label
                    else -> my.rudione.designsystem.R.string.follow_text_label
                },
                onClick = onButtonClick,
                modifier = modifier
                    .heightIn(30.dp)
                    .widthIn(100.dp),
                isOutline = isCurrentUser || isFollowing
            )
        }
    }
}

@Composable
private fun FollowsText(
    modifier: Modifier = Modifier,
    count: Int,
    @StringRes text: Int,
    onClick: () -> Unit
) {

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            ) {
                append(text = "$count ")
            }

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            ) {
                append(text = stringResource(id = text))
            }
        },
        modifier = modifier.clickable { onClick() }
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ProfileHeaderPreview() {
    TranquilityTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ProfileHeaderSection(
                imageUrl = "",
                name = "Denis Rudenko",
                bio = "Hey there, welcome to Denis Rudenko page",
                followersCount = 9,
                followingCount = 2,
                onButtonClick = { /*TODO*/ },
                onFollowersClick = { /*TODO*/ }) {
            }
        }
    }
}