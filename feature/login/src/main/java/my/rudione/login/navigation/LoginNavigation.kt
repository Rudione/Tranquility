package my.rudione.login.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import my.rudione.login.LoginScreen
import my.rudione.login.LoginViewModel
import my.rudione.ui.SharedScreen

class LoginNavigation : Screen {
    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val postSignUpScreen = rememberScreen(SharedScreen.SignUpScreen)
        val postHomeScreen = rememberScreen(SharedScreen.HomeScreen)
        val coroutineScope = rememberCoroutineScope()

        LoginScreen(
            uiState = viewModel.uiState,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onSignInClick = viewModel::signIn,
            onNavigateToHome = {
                coroutineScope.launch {
                    navigator.push(postHomeScreen)
                }
            },
            onNavigateToSignup = {
                coroutineScope.launch {
                    navigator.push(postSignUpScreen)
                }
            }
        )
    }
}