package my.rudione.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import my.rudione.designsystem.theme.Gray

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPasswordTextField: Boolean = false,
    isSingleLine: Boolean = true,
    @StringRes hint: Int
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val visualTransformation = getVisualTransformation(isPasswordTextField, isPasswordVisible)
    val trailingIcon = getTrailingIcon(isPasswordTextField, isPasswordVisible) {
        isPasswordVisible = !isPasswordVisible
    }
    val containerColor = getContainerColor()

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        singleLine = isSingleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                text = stringResource(id = hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        shape = MaterialTheme.shapes.medium
    )
}

@Composable
fun getVisualTransformation(isPasswordTextField: Boolean, isPasswordVisible: Boolean): VisualTransformation {
    return if (isPasswordTextField) {
        if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }
}

@Composable
fun getTrailingIcon(
    isPasswordTextField: Boolean,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit
): @Composable (() -> Unit)? {
    return if (isPasswordTextField) {
        {
            PasswordEyeIcon(isPasswordVisible = isPasswordVisible, onPasswordVisibilityToggle = onPasswordVisibilityToggle)
        }
    } else null
}

@Composable
fun getContainerColor(): Color {
    return if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.surface
    } else {
        Gray
    }
}

@Composable
fun PasswordEyeIcon(
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit
) {
    val image = if (isPasswordVisible) {
        painterResource(id = my.rudione.designsystem.R.drawable.show_eye_icon_filled)
    } else {
        painterResource(id = my.rudione.designsystem.R.drawable.hide_eye_icon_filled)
    }

    IconButton(
        onClick = onPasswordVisibilityToggle,
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            painter = image,
            contentDescription = null
        )
    }
}