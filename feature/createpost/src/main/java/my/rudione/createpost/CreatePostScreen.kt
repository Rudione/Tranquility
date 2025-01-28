package my.rudione.createpost

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.rudione.designsystem.TranquilityIcons
import my.rudione.designsystem.theme.ButtonHeight
import my.rudione.designsystem.theme.ExtraLargeSpacing
import my.rudione.designsystem.theme.ExtraLargeUnderAppBar
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.ui.components.ScreenLevelLoadingView

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    createPostUiState: CreatePostUiState,
    onPostCreated: () -> Unit,
    onUiAction: (CreatePostUiAction) -> Unit,
) {
    val context = LocalContext.current
    var selectedImage by rememberSaveable { mutableStateOf<Uri?>(null) }
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImage = uri }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = ExtraLargeUnderAppBar),
        contentAlignment = Alignment.Center,
    ) {
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
            PostCaptionTextField(
                caption = createPostUiState.caption,
                onCaptionChange = {
                    onUiAction(CreatePostUiAction.CaptionChanged(it))
                }
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LargeSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = my.rudione.designsystem.R.string.select_post_image_label),
                    style = MaterialTheme.typography.headlineMedium
                )

                selectedImage?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = modifier
                            .size(70.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                pickImage.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    IconButton(
                        onClick = {
                            pickImage.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        modifier = modifier
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(TranquilityIcons.ADD_IMAGE),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = modifier.fillMaxSize()
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        selectedImage?.let {
                            onUiAction(CreatePostUiAction.CreatePostAction(it))
                        }
                    },
                    modifier = modifier
                        .weight(0.7f)
                        .height(ButtonHeight),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp
                    ),
                    shape = MaterialTheme.shapes.medium,
                    enabled = createPostUiState.caption.isNotBlank()
                            && !createPostUiState.isLoading
                            && selectedImage != null
                ) {
                    Text(text = stringResource(id = my.rudione.designsystem.R.string.create_post_button_label))
                }
                Spacer(modifier = modifier.width(8.dp))
                Button(
                    onClick = {
                        onUiAction(CreatePostUiAction.CaptionChangedWithAI(createPostUiState.caption))
                    },
                    modifier = modifier
                        .weight(0.15f)
                        .height(ButtonHeight),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(TranquilityIcons.AI_ICON),
                        contentDescription = null,
                        modifier = modifier.fillMaxSize()
                    )
                }
            }
        }

        if (createPostUiState.isLoading) {
            ScreenLevelLoadingView()
        }
    }
    if (createPostUiState.postCreated) {
        onPostCreated()
    }
    if (createPostUiState.errorMessage != null) {
        Toast.makeText(context, createPostUiState.errorMessage, Toast.LENGTH_SHORT).show()
        Log.d("CreatePostScreen", "CreatePostScreen: ${createPostUiState.errorMessage}")
    }
}

@Composable
private fun PostCaptionTextField(
    modifier: Modifier = Modifier,
    caption: String,
    onCaptionChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        value = caption,
        onValueChange = onCaptionChange,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = stringResource(id = my.rudione.designsystem.R.string.post_caption_hint),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = MaterialTheme.shapes.medium
    )
}