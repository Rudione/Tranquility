package my.rudione.ui.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun CircleImage(
    modifier: Modifier,
    url: String?,
    uri: Uri? = null,
    onClick: () -> Unit
) {
    AsyncImage(
        model = uri ?: url,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}