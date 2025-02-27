package my.rudione.follows

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import my.rudione.common.util.toCurrentUrl
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.ui.components.CircleImage

@Composable
fun FollowsListItem(
    modifier: Modifier = Modifier,
    name: String,
    bio: String,
    imageUrl: String?,
    onItemClick:() -> Unit
) {

    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
        horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
        verticalAlignment = Alignment.CenterVertically
    ){

        CircleImage(modifier = modifier.size(40.dp), url = imageUrl?.toCurrentUrl(), onClick = {})

        Column {

            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = bio,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}