package my.rudione.tranquility.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import my.rudione.createpost.navigation.CreatePostNavigation
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.home.navigation.HomeNavigation
import my.rudione.login.navigation.LoginNavigation
import my.rudione.signup.navigation.SignUpNavigation
import my.rudione.ui.SharedScreen
import my.rudione.ui.components.AppBar
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
                    .onEach { uiState = it }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition { uiState == MainActivityUiState.Loading }

        Log.d("MainActivity", "onCreate: $uiState")

        setContent {
            TranquilityTheme {
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

                val userId = (uiState as? MainActivityUiState.Success)?.currentUser?.id
                val pushToProfileScreen = rememberScreen(SharedScreen.ProfileScreen(userId ?: 0))

                Navigator(startScreen) { navigator ->
                    Scaffold(
                        topBar = {
                            AppBar(
                                navigator = navigator,
                                appBar = stringResource(id = getTitleAppBar(navigator.lastItem)),
                                shouldShowNavigationIcon = { shouldShowNavigationIcon(navigator.lastItem) },
                                actionVisible = (navigator.lastItem is HomeNavigation),
                                onClickNavigationProfile = {
                                    navigator.push(pushToProfileScreen)
                                }
                            )
                        },
                        floatingActionButton = {
                            AnimatedVisibility(
                                visible = navigator.lastItem is HomeNavigation
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        navigator.push(CreatePostNavigation())
                                    },
                                    containerColor = MaterialTheme.colorScheme.primary
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    ) {
                        Spacer(modifier = Modifier.size(28.dp))
                        navigator.lastItem.Content()
                    }
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

private fun getTitleAppBar(currentDestination: Screen): Int {
    return when (currentDestination) {
        is HomeNavigation -> my.rudione.designsystem.R.string.home_destination_title
        is CreatePostNavigation -> my.rudione.designsystem.R.string.create_post_destination_title
        is LoginNavigation -> my.rudione.designsystem.R.string.login_destination_title
        is SignUpNavigation -> my.rudione.designsystem.R.string.signup_destination_title
        else -> my.rudione.designsystem.R.string.no_destination_title
    }
}

private fun shouldShowNavigationIcon(currentDestination: Screen?): Boolean {
    return !(
            currentDestination is LoginNavigation
                    || currentDestination is SignUpNavigation
                    || currentDestination is HomeNavigation
            )
}