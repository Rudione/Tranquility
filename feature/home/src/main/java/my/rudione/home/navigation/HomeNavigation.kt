package my.rudione.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import my.rudione.home.HomeRefreshState
import my.rudione.home.HomeScreen
import my.rudione.home.HomeViewModel
import my.rudione.home.PostsUiState
import my.rudione.home.onboarding.OnBoardingUiState

class HomeNavigation : Screen {
    @Composable
    override fun Content() {
        val viewModel: HomeViewModel = koinScreenModel()

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            onBoardingUiState = OnBoardingUiState(),
            postsFeedUiState = PostsUiState(),
            homeRefreshState = HomeRefreshState(),
            onUiAction = {},
            onProfileNavigation = {},
            onPostDetailNavigation = {},
            fetchData = {
                viewModel.fetchData()
            }
        )
    }
}