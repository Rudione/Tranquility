package my.rudione.home.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import my.rudione.common.fake_data.FollowsUser
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.home.R

@Composable
fun OnBoardingSection(
    modifier: Modifier = Modifier,
    users: List<FollowsUser>,
    onUserClick: (FollowsUser) -> Unit,
    onFollowsButtonClick: (Boolean, FollowsUser) -> Unit,
    onBoardingFinish: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(my.rudione.designsystem.R.string.onboarding_title),
            modifier = modifier
                .fillMaxWidth()
                .padding(top = MediumSpacing),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.onboarding_description),
            modifier = modifier
                .fillMaxWidth()
                .padding(top = MediumSpacing),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = modifier.heightIn(MediumSpacing))

        UsersRow(
            users = users,
            onUserClick = onUserClick,
            onFollowsButtonClick = onFollowsButtonClick
        )

        OutlinedButton(
            onClick = onBoardingFinish,
            modifier = modifier
                .fillMaxWidth(fraction = 0.5f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = LargeSpacing),
            shape = RoundedCornerShape(percent = 50)
        ) {
            Text(
                text = stringResource(R.string.onboarding_done_button),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun UsersRow(
    modifier: Modifier = Modifier,
    users: List<FollowsUser>,
    onUserClick: (FollowsUser) -> Unit,
    onFollowsButtonClick: (Boolean, FollowsUser) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
        contentPadding = PaddingValues(start = LargeSpacing, end = LargeSpacing),
        modifier = modifier
    ) {
        items(
            items = users,
            key = { followUser ->
                followUser.id
            }
        ) {
            OnBoardingUserItem(
                followsUser = it,
                onUserClick = onUserClick,
                isFollowing = it.isFollowing,
                onFollowButtonClick = onFollowsButtonClick
            )
        }
    }
}

@Composable
@Preview
fun OnBoardingPreview() {
    TranquilityTheme {
        OnBoardingSection(
            users = listOf(
                FollowsUser(
                    id = 1,
                    name = "Denis Rudenko",
                    profileUrl = "https://avatars.githubusercontent.com/u/1026365?v=4",
                    isFollowing = false
                ),
                FollowsUser(
                    id = 2,
                    name = "John Doe",
                    profileUrl = "https://avatars.githubusercontent.com/u/1026365?v=4",
                    isFollowing = true
                ),
                FollowsUser(
                    id = 3,
                    name = "Jane Doe",
                    profileUrl = "https://avatars.githubusercontent.com/u/1026365?v=4",
                    isFollowing = false
                )
            ),
            onUserClick = {},
            onFollowsButtonClick = { _, _ -> },
            onBoardingFinish = {}
        )
    }
}