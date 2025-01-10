package my.rudione.profile.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import my.rudione.profile.ProfileScreen
import my.rudione.profile.ProfileViewModel
import my.rudione.ui.SharedScreen

class ProfileNavigation(
    private val userId: Long
): Screen {
    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val postEditProfileScreen = rememberScreen(SharedScreen.EditProfileScreen(userId))
        val postFollowsScreen = rememberScreen(SharedScreen.FollowsScreen(userId))
        val postFollowingScreen = rememberScreen(SharedScreen.FollowingScreen(userId))

        ProfileScreen(
            userInfoUiState = viewModel.userInfoUiState,
            profilePostsUiState = viewModel.profilePostsUiState,
            onButtonClick = {
                navigator.push(postEditProfileScreen)
            },
            onFollowersClick = { navigator.push(postFollowsScreen) },
            onFollowingClick = { navigator.push(postFollowingScreen) },
            onPostClick = {},
            onLikeClick = {},
            onCommentClick = {},
            fetchData = { viewModel.fetchProfile(userId)}
        )
    }
}