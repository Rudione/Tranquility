package my.rudione.editprofile.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import my.rudione.editprofile.EditProfileScreen
import my.rudione.editprofile.EditProfileViewModel

class EditProfileNavigation(
    private val userId: Long
): Screen {
    @Composable
    override fun Content() {
        val viewModel: EditProfileViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        EditProfileScreen(
            editProfileUiState = viewModel.uiState,
            bioTextFieldValue = viewModel.bioTextFieldValue,
            onNameChange = viewModel::onNameChange,
            onBioChange = viewModel::onBioChange,
            onUploadButtonClick = { viewModel.uploadProfile() },
            onUploadSucceed = {
                navigator.pop()
            },
            fetchProfile = { viewModel.fetchProfile(userId) }
        )
    }
}