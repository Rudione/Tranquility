package my.rudione.profile

import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.common.domain.model.Post

sealed interface ProfileUiAction {
    data class FetchProfileAction(val profileId: Long): ProfileUiAction

    data class FollowUserAction(val profile: Profile): ProfileUiAction

    data object LoadMorePostsAction: ProfileUiAction

    data class PostLikeAction(val post: Post): ProfileUiAction
}