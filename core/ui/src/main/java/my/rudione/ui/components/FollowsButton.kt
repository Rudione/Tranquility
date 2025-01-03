package my.rudione.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FollowsButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    onClick: () -> Unit,
    isOutline: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = if (isOutline){
            ButtonDefaults.outlinedButtonColors()
        }else{
            ButtonDefaults.buttonColors()
        },
        border = if (isOutline){
            ButtonDefaults.outlinedButtonBorder()
        }else{
            null
        },
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 1.dp
        )
    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp
            )
        )
    }
}

@Composable
@Preview
fun FollowsButtonPreview() {
    FollowsButton(
        text = android.R.string.ok,
        onClick = {},
        isOutline = true
    )
}