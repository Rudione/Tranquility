package my.rudione.createpost.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import my.rudione.createpost.CreatePostScreen
import my.rudione.createpost.CreatePostViewModel

class CreatePostNavigation : Screen {
    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    override fun Content() {
        val viewModel: CreatePostViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        CreatePostScreen(
            modifier = Modifier,
            createPostUiState = viewModel.uiState,
            onPostCreated = { navigator.pop() },
            onUiAction = viewModel::onUiAction
        )
    }
}