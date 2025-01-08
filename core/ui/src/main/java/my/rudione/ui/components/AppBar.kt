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
import cafe.adriel.voyager.navigator.Navigator
import my.rudione.designsystem.TranquilityIcons
import my.rudione.designsystem.theme.SmallElevation
import my.rudione.ui.SharedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navigator: Navigator
) {
    val currentDestination = navigator.lastItem as? SharedScreen

    Surface(
        modifier = modifier,
        tonalElevation = SmallElevation
    ) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = getAppBarTitle(currentDestination)))
            },
            modifier = modifier,
            actions = {
                AnimatedVisibility(visible = currentDestination is SharedScreen.HomeScreen) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = TranquilityIcons.PERSON_CIRCLE_ICON),
                            contentDescription = null
                        )
                    }
                }
            },
            navigationIcon = {
                if (shouldShowNavigationIcon(currentDestination)) {
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

private fun getAppBarTitle(currentDestination: SharedScreen?): Int {
    return when (currentDestination) {
        is SharedScreen.LoginScreen -> my.rudione.designsystem.R.string.login_destination_title
        is SharedScreen.SignUpScreen -> my.rudione.designsystem.R.string.signup_destination_title
        is SharedScreen.HomeScreen -> my.rudione.designsystem.R.string.home_destination_title
        is SharedScreen.ProfileScreen -> my.rudione.designsystem.R.string.profile_destination_title
        is SharedScreen.PostDetailScreen -> my.rudione.designsystem.R.string.post_detail_destination_title
        is SharedScreen.EditProfileScreen -> my.rudione.designsystem.R.string.edit_profile_destination_title
        else -> my.rudione.designsystem.R.string.no_destination_title
    }
}

private fun shouldShowNavigationIcon(currentDestination: SharedScreen?): Boolean {
    return !(
            currentDestination is SharedScreen.LoginScreen
                    || currentDestination is SharedScreen.SignUpScreen
                    || currentDestination is SharedScreen.HomeScreen
            )
}