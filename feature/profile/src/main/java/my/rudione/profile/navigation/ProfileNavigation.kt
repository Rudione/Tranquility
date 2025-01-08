package my.rudione.profile.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import my.rudione.profile.ProfileScreen
import my.rudione.profile.ProfileViewModel

class ProfileNavigation(
    private val userId: Long
): Screen {
    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = koinScreenModel()

        ProfileScreen(
            userInfoUiState = viewModel.userInfoUiState,
            profilePostsUiState = viewModel.profilePostsUiState,
            onButtonClick = { },
            onFollowersClick = { },
            onFollowingClick = { },
            onPostClick = {},
            onLikeClick = {},
            onCommentClick = {},
            fetchData = { viewModel.fetchProfile(userId)}
        )
    }
}