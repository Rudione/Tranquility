package my.rudione.tranquility.account.domain.usecase

import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.account.domain.repository.ProfileRepository
import my.rudione.tranquility.common.util.Result
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetProfileUseCase : KoinComponent {
    private val repository: ProfileRepository by inject()
    operator fun invoke(profileId: Long): Flow<Result<Profile>> {
        return repository.getProfile(profileId = profileId)
    }
}