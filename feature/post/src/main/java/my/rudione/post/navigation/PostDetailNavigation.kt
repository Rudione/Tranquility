package my.rudione.post.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import my.rudione.post.PostDetailScreen
import my.rudione.post.PostDetailViewModel
import my.rudione.ui.SharedScreen

class PostDetailNavigation(
    private val postId: Long
) : Screen {

    override val key = "PostDetailNavigation"

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val viewModel: PostDetailViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val postHomeScreen = rememberScreen(SharedScreen.HomeScreen)

        Scaffold(
            topBar = {},
            content = {
                PostDetailScreen(
                    postUiState = viewModel.postUiState,
                    commentsUiState = viewModel.commentsUiState,
                    onCommentMoreIconClick = {},
                    onProfileClick = {

                    },
                    onAddCommentClick = {},
                    fetchData = { viewModel.fetchData(postId) }
                )
            }
        )
    }
}