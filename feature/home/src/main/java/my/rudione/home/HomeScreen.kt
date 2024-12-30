package my.rudione.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import my.rudione.common.fake_data.Post
import my.rudione.common.fake_data.sampleUsers
import my.rudione.common.util.Constants
import my.rudione.designsystem.theme.LargeSpacing
import my.rudione.designsystem.theme.MediumSpacing
import my.rudione.designsystem.theme.TranquilityTheme
import my.rudione.home.onboarding.OnBoardingSection
import my.rudione.home.onboarding.OnBoardingUiState
import my.rudione.ui.components.PostListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onBoardingUiState: OnBoardingUiState,
    postsFeedUiState: PostsUiState,
    homeRefreshState: HomeRefreshState,
    onUiAction: (HomeUiAction) -> Unit,
    onProfileNavigation: (userId: Long) -> Unit,
    onPostDetailNavigation: (Post) -> Unit,
    fetchData : () -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    val listState = rememberLazyListState()
    val shouldFetchMorePosts by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullToRefresh(
                state = pullRefreshState,
                isRefreshing = homeRefreshState.isRefreshing,
                onRefresh = {
                    fetchData()
                }
            )
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState
        ) {
            if (onBoardingUiState.shouldShowOnBoarding) {
                item(key = Constants.ONBOARDING_ITEM_KEY) {
                    OnBoardingSection(
                        users = onBoardingUiState.users,
                        onUserClick = { onProfileNavigation(it.id) },
                        onFollowsButtonClick = { _, user ->
                            onUiAction(
                                HomeUiAction.FollowUserAction(user)
                            )
                        },
                        onBoardingFinish = { onUiAction(HomeUiAction.RemoveOnboardingAction) }
                    )

                    Text(
                        text = stringResource(id = my.rudione.designsystem.R.string.trending_posts_title),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = LargeSpacing),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(items = postsFeedUiState.posts, key = { post -> post.id }) { post ->
                PostListItem(
                    post = post,
                    onPostClick = { onPostDetailNavigation(it) },
                    onProfileClick = { onProfileNavigation(it) },
                    onLikeClick = { onUiAction(HomeUiAction.PostLikeAction(it)) },
                    onCommentClick = { onPostDetailNavigation(it) }
                )
            }

            if (postsFeedUiState.isLoading && postsFeedUiState.posts.isNotEmpty()) {
                item(key = Constants.LOADING_MORE_ITEM_KEY) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = MediumSpacing, horizontal = LargeSpacing),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        LaunchedEffect(key1 = shouldFetchMorePosts) {
            if (shouldFetchMorePosts && !postsFeedUiState.endReached) {
                onUiAction(HomeUiAction.LoadMorePostsAction)
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    TranquilityTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                onBoardingUiState = OnBoardingUiState(isLoading = false, users = sampleUsers),
                postsFeedUiState = PostsUiState(),
                homeRefreshState = HomeRefreshState(),
                onPostDetailNavigation = {},
                onProfileNavigation = {},
                onUiAction = {},
                fetchData = {}
            )
        }
    }
}