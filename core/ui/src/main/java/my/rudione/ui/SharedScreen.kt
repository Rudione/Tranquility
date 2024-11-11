package my.rudione.ui

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    object SignUpScreen : SharedScreen()
    object LoginScreen : SharedScreen()
    object HomeScreen : SharedScreen()
}