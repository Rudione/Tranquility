package my.rudione.ui.flyweight.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import my.rudione.ui.flyweight.Flyweight

/**
 * Then it's time for the Concrete Flyweight layer.
 * We will store the intrinsic state in this layer.
 * In our case, this will be an icon. At the same time,
 * we @override the createWidget method by implementing the Flyweight layer.
 */

class IconFlyweight(
    private val icon: ImageVector
) : Flyweight {
    @Composable
    override fun Render(color: Color, size: Dp, modifier: Modifier) {
        Icon(
            imageVector = icon, tint = color,
            modifier = Modifier.size(size),
            contentDescription = null
        )
    }
}