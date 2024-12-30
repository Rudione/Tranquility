package my.rudione.ui

import cafe.adriel.voyager.core.registry.ScreenProvider
import my.rudione.tranquility.common.domain.model.Post

sealed class SharedScreen(val route: String) : ScreenProvider {
    object SignUpScreen : SharedScreen("signUp")
    object LoginScreen : SharedScreen("login")
    object HomeScreen : SharedScreen("home")
    data class PostDetailScreen(val postId: Post) : SharedScreen(route = "postDetail/$postId")
}