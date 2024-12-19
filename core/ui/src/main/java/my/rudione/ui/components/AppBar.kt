package my.rudione.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import my.rudione.designsystem.TranquilityIcons
import my.rudione.designsystem.theme.SmallElevation
import my.rudione.ui.SharedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val currentDestination = navigator.lastItem

    Surface(
        modifier = modifier,
        tonalElevation = SmallElevation
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = getAppBarTitle(currentDestination.key)))
            },
            modifier = modifier,
            actions = {
                AnimatedVisibility(visible = currentDestination.key == SharedScreen.HomeScreen.route) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = TranquilityIcons.PERSON_CIRCLE_ICON),
                            contentDescription = null
                        )
                    }
                }
            },
            navigationIcon = {
                if (shouldShowNavigationIcon(currentDestination.key)) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            painter = painterResource(id = TranquilityIcons.ROUND_ARROW_BACK),
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}

private fun getAppBarTitle(currentDestinationRoute: String?): Int {
    return when (currentDestinationRoute) {
        SharedScreen.LoginScreen.route -> my.rudione.designsystem.R.string.login_destination_title
        SharedScreen.SignUpScreen.route -> my.rudione.designsystem.R.string.signup_destination_title
        SharedScreen.HomeScreen.route -> my.rudione.designsystem.R.string.home_destination_title
        else -> my.rudione.designsystem.R.string.no_destination_title
    }
}

private fun shouldShowNavigationIcon(currentDestinationRoute: String?): Boolean {
    return !(currentDestinationRoute == SharedScreen.LoginScreen.route
            || currentDestinationRoute == SharedScreen.SignUpScreen.route
            || currentDestinationRoute == SharedScreen.HomeScreen.route
            || currentDestinationRoute == null
            )
}