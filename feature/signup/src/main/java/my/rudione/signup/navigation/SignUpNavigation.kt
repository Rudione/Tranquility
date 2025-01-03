package my.rudione.signup.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import my.rudione.signup.SignUpScreen
import my.rudione.signup.SignUpViewModel
import my.rudione.ui.SharedScreen

class SignUpNavigation : Screen {

    override val key = "SignUpNavigation"

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val viewModel: SignUpViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val postLoginScreen = rememberScreen(SharedScreen.LoginScreen)
        val postHomeScreen = rememberScreen(SharedScreen.HomeScreen)
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            topBar = {},
            content = {
                SignUpScreen(
                    uiState = viewModel.uiState,
                    onUsernameChange = viewModel::updateUsername,
                    onEmailChange = viewModel::updateEmail,
                    onPasswordChange = viewModel::updatePassword,
                    onNavigateToLogin = {
                        navigator.push(postLoginScreen)
                    },
                    onNavigateToHome = {
                        navigator.push(postHomeScreen)
                    },
                    onSignUpClick = {
                        coroutineScope.launch {
                            viewModel.signUp()
                            Log.d("SignUpNavigation", "Content: signUp")
                        }
                    }
                )
            }
        )
    }
}