package my.rudione.ui

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreen : ScreenProvider {
    object SignUpScreen : SharedScreen()
    object LoginScreen : SharedScreen()
    object HomeScreen : SharedScreen()
    data class PostDetailScreen(val id: Long) : SharedScreen()
    data class ProfileScreen(val id: Long) : SharedScreen()
    data class EditProfileScreen(val id: Long) : SharedScreen()
    data class FollowsScreen(val id: Long) : SharedScreen()
    data class FollowingScreen(val id: Long) : SharedScreen()
}