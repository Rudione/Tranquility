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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import my.rudione.common.fake_data.Comment
import my.rudione.common.fake_data.sampleComments
import my.rudione.designsystem.theme.DarkGray
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.LightGray
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.TranquilityTheme

@Composable
fun CommentListItem(
    modifier: Modifier = Modifier,
    comment: Comment,
    onProfileClick: (Long) -> Unit,
    onMoreIconClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { }
            .padding(all = LargeSpacing),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {
        CircleImage(
            modifier = modifier.size(30.dp),
            url = comment.authorImageUrl,
            onClick = { onProfileClick(comment.authorId) }
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
                    text = comment.authorName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.alignByBaseline()
                )

                Text(
                    text = comment.date,
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
                    painter = painterResource(id = my.rudione.designsystem.R.drawable.round_more_horizontal),
                    contentDescription = null,
                    tint = if (isSystemInDarkTheme()) {
                        DarkGray
                    } else {
                        LightGray
                    },
                    modifier = modifier.clickable { onMoreIconClick() }
                )
            }

            Text(
                text = comment.comment,
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
                comment = sampleComments.first(),
                onProfileClick = {},
                onMoreIconClick = {}
            )
        }
    }
}


@Composable
fun CommentItemLayout(
    modifier: Modifier,
    image: @Composable () -> Unit,
    username: @Composable () -> Unit,
    date: @Composable () -> Unit,
    moreIcon: @Composable () -> Unit,
    commentText: @Composable () -> Unit
) {
    Layout(
        contents = listOf(
            image,
            username,
            date,
            moreIcon,
            commentText
        ),
        modifier = modifier
            .padding(
                top = LargeSpacing,
                bottom = LargeSpacing,
                start = LargeSpacing,
                end = MediumSpacing
            )
    ) { (imageMeasurables,
            usernameMeasurables,
            dateMeasurables,
            moreIconMeasurables,
            commentTextMeasurables),
        constraints ->

        val imagePlaceable = imageMeasurables.first().measure(constraints)
        val usernamePlaceable = usernameMeasurables.first().measure(constraints)
        val datePlaceable = dateMeasurables.first().measure(constraints)
        val moreIconPlaceable = moreIconMeasurables.first().measure(constraints)

        val commentTextConstraints = constraints.copy(
            maxWidth = constraints.maxWidth - (24.dp.roundToPx() + imagePlaceable.width)
        )
        val commentTextPlaceable = commentTextMeasurables.first().measure(commentTextConstraints)

        val totalHeight = maxOf(
            imagePlaceable.height,
            (usernamePlaceable.height + commentTextPlaceable.height)
        )

        layout(width = constraints.maxWidth, height = totalHeight) {
            imagePlaceable.placeRelative(x = 0, y = 0)
            usernamePlaceable.placeRelative(
                x = imagePlaceable.width + (8.dp.roundToPx()),
                y = 0
            )
            datePlaceable.placeRelative(
                x = imagePlaceable.width + usernamePlaceable.width + (16.dp.roundToPx()),
                y = 3.dp.roundToPx()
            )

            moreIconPlaceable.placeRelative(
                x = (constraints.maxWidth) - (moreIconPlaceable.width),
                y = 0
            )

            commentTextPlaceable.place(
                x = imagePlaceable.width + (8.dp.roundToPx()),
                y = usernamePlaceable.height + (2.dp.roundToPx())
            )
        }
    }
}