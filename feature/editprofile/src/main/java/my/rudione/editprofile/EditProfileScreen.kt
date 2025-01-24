package my.rudione.editprofile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import my.rudione.common.util.toCurrentUrl
import my.rudione.designsystem.theme.ButtonHeight
import my.rudione.designsystem.theme.ExtraLargeSpacing
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.SmallElevation
import my.rudione.ui.components.CircleImage
import my.rudione.ui.components.CustomTextField
import my.rudione.ui.components.ScreenLevelLoadingErrorView
import my.rudione.ui.components.ScreenLevelLoadingView

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    editProfileUiState: EditProfileUiState,
    onNameChange: (String) -> Unit,
    bioTextFieldValue: TextFieldValue,
    onBioChange: (TextFieldValue) -> Unit,
    onUploadSucceed: () -> Unit,
    userId: Long,
    onUiAction: (EditProfileUiAction) -> Unit
) {

    val context = LocalContext.current
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImage = uri }
    )

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (editProfileUiState.profile != null){
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .padding(ExtraLargeSpacing),
                verticalArrangement = Arrangement.spacedBy(LargeSpacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box {
                    CircleImage(
                        modifier = modifier.size(120.dp),
                        url = editProfileUiState.profile.imageUrl?.toCurrentUrl(),
                        uri = selectedImage,
                        onClick = {}
                    )

                    IconButton(
                        onClick = {
                            pickImage.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        modifier = modifier
                            .align(Alignment.BottomEnd)
                            .shadow(
                                elevation = SmallElevation,
                                shape = RoundedCornerShape(percent = 25)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(percent = 25)
                            )
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = modifier.height(LargeSpacing))

                CustomTextField(
                    value = editProfileUiState.profile.name,
                    onValueChange = onNameChange,
                    hint = my.rudione.designsystem.R.string.username_hint
                )

                BioTextField(value = bioTextFieldValue, onValueChange = onBioChange)

                Button(
                    onClick = {
                        selectedImage?.let {
                            onUiAction(EditProfileUiAction.UploadProfileAction(imageUri = it))
                        } ?: run {
                            // No image selected, proceed with no image
                            onUiAction(EditProfileUiAction.UploadProfileAction())
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .height(ButtonHeight),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 0.dp
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(id = my.rudione.designsystem.R.string.upload_changes_text))
                }

            }
        }

        if (editProfileUiState.profile == null && editProfileUiState.errorMessage != null){
            ScreenLevelLoadingErrorView {
                onUiAction(EditProfileUiAction.FetchProfileAction(userId = userId))
            }
        }

        if (editProfileUiState.isLoading) {
            ScreenLevelLoadingView()
        }

        if (editProfileUiState.isLoading){
            CircularProgressIndicator()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        onUiAction(EditProfileUiAction.FetchProfileAction(userId = userId))
    })

    LaunchedEffect(
        key1 = editProfileUiState.uploadSucceed,
        key2 = editProfileUiState.errorMessage,
        block = {
            if (editProfileUiState.uploadSucceed){
                onUploadSucceed()
            }

            if (editProfileUiState.profile != null && editProfileUiState.errorMessage != null){
                Toast.makeText(context, editProfileUiState.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    )

}

@Composable
fun BioTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        value = value,
        onValueChange = {
            onValueChange(
                TextFieldValue(
                    text = it.text,
                    selection = TextRange(it.text.length)
                )
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.background
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = stringResource(id = my.rudione.designsystem.R.string.user_bio_hint),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = MaterialTheme.shapes.medium
    )
}