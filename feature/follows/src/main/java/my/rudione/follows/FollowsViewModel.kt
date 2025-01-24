package my.rudione.follows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.rudione.common.fake_data.sampleUsers
import my.rudione.common.util.Constants
import my.rudione.common.util.DefaultPagingManager
import my.rudione.common.util.PagingManager
import my.rudione.tranquility.common.domain.model.FollowsUser
import my.rudione.tranquility.follows.domain.usecase.GetFollowsUseCase

class FollowsViewModel(
    private val getFollowsUseCase: GetFollowsUseCase
): ScreenModel {

    var uiState by mutableStateOf(FollowsUiState())
        private set

    private lateinit var pagingManager: PagingManager<FollowsUser>

    private fun fetchFollows(userId: Long, followsType: Int){
        screenModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            delay(1000)
            if (!::pagingManager.isInitialized) {
                pagingManager = createPagingManager(userId, followsType)
                pagingManager.loadItems()
            }
        }
    }

    private fun createPagingManager(userId: Long, followsType: Int): PagingManager<FollowsUser> {
        return DefaultPagingManager(
            onRequest = { page ->
                getFollowsUseCase(
                    userId = userId,
                    page = page,
                    pageSize = Constants.DEFAULT_REQUEST_PAGE_SIZE,
                    followsType = followsType
                )
            },
            onSuccess = { follows, _ ->
                uiState = uiState.copy(
                    isLoading = false,
                    followsUsers = uiState.followsUsers + follows,
                    endReached = follows.size < Constants.DEFAULT_REQUEST_PAGE_SIZE
                )
            },
            onError = { message, _ ->
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = message
                )
            },
            onLoadStateChange = {
                uiState = uiState.copy(isLoading = it)
            }
        )
    }

    private fun loadMoreFollows() {
        if (uiState.endReached) return
        screenModelScope.launch {
            pagingManager.loadItems()
        }
    }

    fun onUiAction(action: FollowsUiAction) {
        when (action) {
            is FollowsUiAction.FetchFollowsAction -> {
                fetchFollows(action.userId, action.followsType)
            }
            is FollowsUiAction.LoadMoreFollowsAction -> {
                loadMoreFollows()
            }
        }
    }
}

sealed interface FollowsUiAction{
    data class FetchFollowsAction(val userId: Long, val followsType: Int): FollowsUiAction
    data object LoadMoreFollowsAction: FollowsUiAction
}