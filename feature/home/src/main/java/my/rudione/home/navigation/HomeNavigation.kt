package my.rudione.home.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import my.rudione.home.HomeScreen

class HomeNavigation : Screen {
    @Composable
    override fun Content() {

        HomeScreen()
    }
}