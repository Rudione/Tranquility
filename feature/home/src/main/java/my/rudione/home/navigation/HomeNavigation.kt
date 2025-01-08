package my.rudione.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import my.rudione.home.HomeRefreshState
import my.rudione.home.HomeScreen
import my.rudione.home.HomeViewModel
import my.rudione.home.PostsUiState
import my.rudione.home.onboarding.OnBoardingUiState
import my.rudione.post.navigation.PostDetailNavigation
import my.rudione.profile.navigation.ProfileNavigation

class HomeNavigation : Screen {

    override val key = "HomeNavigation"

    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            onBoardingUiState = OnBoardingUiState(),
            postsFeedUiState = PostsUiState(),
            homeRefreshState = HomeRefreshState(),
            onUiAction = {},
            onProfileNavigation = { it ->
                navigator.push(ProfileNavigation(it))
            },
            onPostDetailNavigation = { it ->
                navigator.push(PostDetailNavigation(it.id))
            },
            fetchData = {
                viewModel.fetchData()
            }
        )
    }
}
