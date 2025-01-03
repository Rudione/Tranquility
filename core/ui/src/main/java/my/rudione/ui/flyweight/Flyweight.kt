package my.rudione.ui.flyweight

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Instead of creating the same icons over and over again for actions such as comments,
 * likes and shares on each post, we will optimize them with the Flyweight design pattern.
 *
 * First, we start by making the Flyweight Interface layer.
 * We place a method with the method signature Widget createWidget(Color color, double size), which returns Widget.
 */

interface Flyweight {
    @Composable
    fun Render(color: Color, size: Dp, modifier: Modifier)
}