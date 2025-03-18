package my.rudione.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import my.rudione.common.fake_data.sampleSamplePosts
import my.rudione.common.util.toCurrentUrl
import my.rudione.designsystem.TranquilityIcons
import my.rudione.designsystem.theme.DarkGray
import my.rudione.designsystem.theme.ExtraLargeSpacing
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.LightGray
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.tranquility.common.domain.model.Post
import my.rudione.ui.flyweight.icon.IconFactory

@Composable
fun PostListItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPostClick: ((Post) -> Unit)? = null,
    onProfileClick: (userId: Long) -> Unit,
    onLikeClick: (Post) -> Unit,
    onCommentClick: (Post) -> Unit,
    maxLines: Int = Int.MAX_VALUE
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            //.aspectRatio(ratio = 0.7f)
            .background(color = MaterialTheme.colorScheme.surface)
            //.clickable { onPostClick(post) }
            .let { mod ->
                if (onPostClick != null) {
                    mod.clickable { onPostClick(post) }.padding(bottom = ExtraLargeSpacing)
                } else {
                    mod
                }
            }
    ) {
        PostHeader(
            name = post.userName,
            profileUrl = post.userImageUrl,
            date = post.createdAt,
            onProfileClick = {
                onProfileClick(
                    post.userId
                )
            }
        )

        AsyncImage(
            model = post.imageUrl.toCurrentUrl(),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.0f),
            contentScale = ContentScale.Crop,
            placeholder = if (isSystemInDarkTheme()) {
                painterResource(id = my.rudione.designsystem.R.drawable.dark_image_placeholder)
            } else {
                painterResource(id = my.rudione.designsystem.R.drawable.light_image_placeholder)
            }
        )

        PostLikesRow(
            likesCount = post.likesCount,
            commentCount = post.commentsCount,
            onLikeClick = { onLikeClick(post) },
            isPostLiked = post.isLiked,
            onCommentClick = { onCommentClick(post) }
        )

        Text(
            text = post.caption,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = LargeSpacing),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun PostHeader(
    modifier: Modifier = Modifier,
    name: String,
    profileUrl: String?,
    date: String,
    onProfileClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {
        CircleImage(
            modifier = modifier.size(30.dp),
            url = profileUrl?.toCurrentUrl(),
            onClick = onProfileClick
        )

        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(
                    color = if (isSystemInDarkTheme()) {
                        DarkGray
                    } else {
                        LightGray
                    }
                )
        )

        Text(
            text = date,
            style = MaterialTheme.typography.labelSmall.copy(
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                color = if (isSystemInDarkTheme()) {
                    DarkGray
                } else {
                    LightGray
                }
            ),
            modifier = modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = my.rudione.designsystem.R.drawable.round_more_horizontal),
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) {
                DarkGray
            } else {
                LightGray
            },
            modifier = modifier.clickable {

            }
        )
    }
}

@Composable
fun PostLikesRow(
    modifier: Modifier = Modifier,
    likesCount: Int,
    commentCount: Int,
    onLikeClick: () -> Unit,
    isPostLiked: Boolean,
    onCommentClick: () -> Unit
) {
    val commentIcon = IconFactory.getIcon(
        ImageVector.vectorResource(TranquilityIcons.CHAT_ICON_OUTLINED)
    )

    val likeIcon = if (isPostLiked) {
        IconFactory.getIcon(
            ImageVector.vectorResource(TranquilityIcons.LIKE_ICON_FILLED)
        )
    } else {
        IconFactory.getIcon(
            ImageVector.vectorResource(TranquilityIcons.LIKE_ICON_OUTLINED)
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 0.dp,
                horizontal = MediumSpacing
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onLikeClick
        ) {
            likeIcon.Render(
                color = if (isPostLiked) Red else DarkGray,
                size = 24.dp,
                modifier = modifier
            )
        }

        Text(
            text = "$likesCount",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )

        Spacer(modifier = modifier.width(MediumSpacing))

        IconButton(
            onClick = onCommentClick
        ) {
            commentIcon.Render(
                color = if (isSystemInDarkTheme()) {
                    DarkGray
                } else {
                    LightGray
                },
                size = 24.dp,
                modifier = modifier
            )
        }

        Text(
            text = "$commentCount",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )
    }
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PostListItemPreview() {
    TranquilityTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            PostListItem(
                post = sampleSamplePosts.first().toDomainPost(),
                onPostClick = {},
                onProfileClick = {},
                onCommentClick = {},
                onLikeClick = {}
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PostHeaderPreview() {
    TranquilityTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            PostHeader(
                name = "Denis Rudenko",
                profileUrl = "",
                date = "20 min",
                onProfileClick = {}
            )
        }
    }
}


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PostLikesRowPreview() {
    TranquilityTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            PostLikesRow(
                likesCount = 12,
                commentCount = 2,
                onLikeClick = {},
                isPostLiked = true,
                onCommentClick = {}
            )
        }
    }
}