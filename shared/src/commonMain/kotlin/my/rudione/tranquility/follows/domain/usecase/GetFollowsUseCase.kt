package my.rudione.tranquility.follows.domain.usecase

import my.rudione.tranquility.common.domain.model.FollowsUser
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.follows.domain.FollowsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetFollowsUseCase: KoinComponent {
    private val followsRepository: FollowsRepository by inject()

    suspend operator fun invoke(
        userId: Long,
        page: Int,
        pageSize: Int,
        followsType: Int
    ): Result<List<FollowsUser>> {
        return followsRepository.getFollows(userId, page, pageSize, followsType)
    }
}