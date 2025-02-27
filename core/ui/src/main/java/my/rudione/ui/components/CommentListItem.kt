package my.rudione.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.rudione.common.fake_data.Comment
import my.rudione.common.fake_data.sampleComments
import my.rudione.common.util.toCurrentUrl
import my.rudione.designsystem.theme.DarkGray
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.LightGray
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.tranquility.post.domain.model.PostComment
import my.rudione.ui.flyweight.icon.IconFactory

@Composable
fun CommentListItem(
    modifier: Modifier = Modifier,
    comment: PostComment,
    onProfileClick: (Long) -> Unit,
    onMoreIconClick: (PostComment) -> Unit
) {
    val roundMoreIcon = IconFactory.getIcon(ImageVector.vectorResource(id = my.rudione.designsystem.R.drawable.round_more_horizontal))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(all = LargeSpacing),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {
        CircleImage(
            modifier = modifier.size(30.dp),
            url = comment.userImageUrl?.toCurrentUrl(),
            onClick = { onProfileClick(comment.userId) }
        )

        Column(
            modifier = modifier
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MediumSpacing
                )
            ) {
                Text(
                    text = comment.userName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.alignByBaseline()
                )

                Text(
                    text = comment.createdAt,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = if (isSystemInDarkTheme()) {
                        DarkGray
                    } else {
                        LightGray
                    },
                    modifier = modifier
                        .alignByBaseline()
                        .weight(1f)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = my.rudione.designsystem.R.drawable.round_more_horizontal),
                    contentDescription = "More",
                    tint = if (isSystemInDarkTheme()) DarkGray else LightGray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onMoreIconClick(comment) }
                )
            }

            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CommentListItemPreview() {
    TranquilityTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CommentListItem(
                comment = sampleComments.first().toDomainComment(),
                onProfileClick = {},
                onMoreIconClick = {}
            )
        }
    }
}