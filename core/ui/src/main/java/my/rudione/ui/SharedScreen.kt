package my.rudione.ui

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen(val route: String) : ScreenProvider {
    object SignUpScreen : SharedScreen("signUp")
    object LoginScreen : SharedScreen("login")
    object HomeScreen : SharedScreen("home")
}