package my.rudione.follows.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import my.rudione.follows.FollowsScreen
import my.rudione.follows.FollowsViewModel
import my.rudione.ui.SharedScreen

class FollowingNavigation(
    private val userId: Long
): Screen {
    @Composable
    override fun Content() {
        val viewModel: FollowsViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val pushToProfile = rememberScreen(SharedScreen.ProfileScreen(userId))

        FollowsScreen(
            uiState = viewModel.uiState,
            fetchFollows = { viewModel.fetchFollows(userId, followsType = 2) },
            onItemClick = {
                navigator.push(pushToProfile)
            }
        )
    }
}