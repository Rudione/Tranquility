package my.rudione.home.onboarding

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import my.rudione.common.fake_data.FollowsUser
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.SmallSpacing
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.home.R
import my.rudione.ui.components.CircleImage
import my.rudione.ui.components.FollowsButton

@Composable
fun OnBoardingUserItem(
    modifier: Modifier = Modifier,
    followsUser: FollowsUser,
    onUserClick: (FollowsUser) -> Unit,
    isFollowing: Boolean = false,
    onFollowButtonClick: (Boolean, FollowsUser) -> Unit
) {
    Card(
        modifier = modifier
            .size(height = 140.dp, width = 130.dp)
            .clickable {
                onUserClick(followsUser)
            }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(MediumSpacing)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.medium
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleImage(
                modifier = modifier.size(50.dp),
                url = followsUser.profileUrl,
                onClick = {}
            )

            Spacer(modifier = modifier.height(SmallSpacing))

            Text(
                text = followsUser.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(MediumSpacing))

            FollowsButton(
                modifier = modifier
                    .widthIn(min = 80.dp)
                    .heightIn(min = 30.dp),
                text = if (isFollowing) R.string.follow else R.string.unfollow,
                onClick = { onFollowButtonClick(isFollowing, followsUser) },
                isOutline = !isFollowing
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingUserPreview() {
    TranquilityTheme {
        OnBoardingUserItem(
            followsUser = FollowsUser(
                id = 1,
                name = "Denis Rudenko",
                profileUrl = "https://picsum.photos/200"
            ),
            onUserClick = {},
            onFollowButtonClick = {_, _ -> 1 })
    }
}