package my.rudione.tranquility.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.home.navigation.HomeNavigation
import my.rudione.signup.navigation.SignUpNavigation
import my.rudione.ui.components.AppBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TranquilityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val tokenState by viewModel.authState.collectAsStateWithLifecycle(initialValue = "")
                    val systemUiController = rememberSystemUiController()
                    val isDarkTheme = isSystemInDarkTheme()

                    UpdateStatusBar(systemUiController, isDarkTheme)

                    val startScreen = if (tokenState.isNotEmpty()) {
                        HomeNavigation()
                    } else {
                        SignUpNavigation()
                    }

                    Navigator(screen = startScreen) { navigator ->
                        Column {
                            AppBar(navigator = navigator)
                            SlideTransition(navigator = navigator)
                        }
                    }


                }
            }
        }

    }
}

@Composable
private fun UpdateStatusBar(systemUiController: com.google.accompanist.systemuicontroller.SystemUiController, isDarkTheme: Boolean) {
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