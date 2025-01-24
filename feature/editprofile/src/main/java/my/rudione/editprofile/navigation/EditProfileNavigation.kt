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
            onNameChange = viewModel::onNameChange,
            bioTextFieldValue = viewModel.bioTextFieldValue,
            onBioChange = viewModel::onBioChange,
            onUploadSucceed = { navigator.pop() },
            userId = userId,
            onUiAction = viewModel::onUiAction
        )
    }
}