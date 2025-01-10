package my.rudione.tranquility.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.home.navigation.HomeNavigation
import my.rudione.login.navigation.LoginNavigation
import my.rudione.signup.navigation.SignUpNavigation
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by inject()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            uiState == MainActivityUiState.Loading
        }

        setContent {
            TranquilityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val systemUiController = rememberSystemUiController()
                    val isDarkTheme = isSystemInDarkTheme()

                    UpdateStatusBar(systemUiController, isDarkTheme)

                    val startScreen = when (uiState) {
                        is MainActivityUiState.Success -> {
                            if ((uiState as MainActivityUiState.Success).currentUser.token.isNotEmpty()) {
                                HomeNavigation()
                            } else {
                                LoginNavigation()
                            }
                        }
                        else -> SignUpNavigation()
                    }

                    Navigator(screen = startScreen)
                }
            }
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