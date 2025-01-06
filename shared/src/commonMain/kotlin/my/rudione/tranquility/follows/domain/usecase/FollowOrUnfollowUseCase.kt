package my.rudione.tranquility.follows.domain.usecase

import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.follows.domain.FollowsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FollowOrUnfollowUseCase: KoinComponent {
    private val repository by inject<FollowsRepository>()

    suspend operator fun invoke(
        followedUserId: Long,
        shouldFollow: Boolean
    ): Result<Boolean> {
        return repository.followOrUnfollow(followedUserId, shouldFollow)
    }
}