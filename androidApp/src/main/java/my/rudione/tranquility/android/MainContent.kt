import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import my.rudione.tranquility.android.MainViewModel
import my.rudione.ui.SharedScreen
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainContent : Screen {
    @Composable
    override fun Content() {
        val viewModel: MainViewModel = koinViewModel()
        val token = viewModel.authState.collectAsStateWithLifecycle(initialValue = "")
        val systemUiController = rememberSystemUiController()
        val postHomeScreen = rememberScreen(SharedScreen.HomeScreen)
        val postSignUpScreen = rememberScreen(SharedScreen.SignUpScreen)

        val isDarkTheme = isSystemInDarkTheme()
        UpdateStatusBar(systemUiController, isDarkTheme)

        Log.d("MainContent", "Token value: ${token.value}")

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Navigator(screen = if (token.value.isEmpty()) {
                Log.d("MainContent", "Navigating to SignUpScreen")
                postSignUpScreen
            } else {
                Log.d("MainContent", "Navigating to HomeScreen")
                postHomeScreen
            })
        }
    }
}

@Composable
private fun UpdateStatusBar(systemUiController: SystemUiController, isDarkTheme: Boolean) {
    val statusBarColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    }
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = !isDarkTheme
        )
    }
}