package my.rudione.ui.components

import android.util.Log
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
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import my.rudione.designsystem.TranquilityIcons
import my.rudione.designsystem.theme.SmallElevation
import my.rudione.ui.SharedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    appBar: String,
    shouldShowNavigationIcon: (Screen) -> Boolean,
    actionVisible: Boolean = true,
    onClickNavigationProfile: () -> Unit,
) {
    val currentDestination = navigator.lastItem


    Surface(
        modifier = modifier,
        tonalElevation = SmallElevation
    ) {
        TopAppBar(
            title = {
                Text(text = appBar)
            },
            modifier = modifier,
            actions = {
                AnimatedVisibility(visible = actionVisible) {
                    IconButton(onClick = onClickNavigationProfile) {
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