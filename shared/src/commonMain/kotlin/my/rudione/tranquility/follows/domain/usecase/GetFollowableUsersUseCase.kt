package my.rudione.tranquility.follows.domain.usecase

import my.rudione.tranquility.common.domain.model.FollowsUser
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.follows.domain.FollowsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetFollowableUsersUseCase: KoinComponent {
    private val repository by inject<FollowsRepository>()

    suspend operator fun invoke(): Result<List<FollowsUser>> {
        return repository.getFollowableUsers()
    }
}