package my.rudione.ui.flyweight.icon

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * It's time to create Flyweight objects in the Flyweight Factory layer.
 * Here, if there is a previously created object, icons are pulled from the map.
 * If it is an object that comes for the first time, it is added to the map.
 */

object IconFactory {
    private val icons = mutableMapOf<ImageVector, IconFlyweight>()

    fun getIcon(icon: ImageVector): IconFlyweight {
        return icons.getOrPut(icon) { IconFlyweight(icon) }
    }
}