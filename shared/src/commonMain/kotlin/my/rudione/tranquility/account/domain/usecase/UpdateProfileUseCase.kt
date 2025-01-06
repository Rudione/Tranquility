package my.rudione.tranquility.account.domain.usecase

import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.account.domain.repository.ProfileRepository
import my.rudione.tranquility.common.util.Constants
import my.rudione.tranquility.common.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateProfileUseCase: KoinComponent {
    private val profileRepository: ProfileRepository by inject()

    suspend operator fun invoke(profile: Profile, imageBytes: ByteArray?): Result<Profile> {
        with(profile.name){
            if (isBlank() || length < 3 || length > 20){
                return Result.Error(message = Constants.INVALID_INPUT_NAME_MESSAGE)
            }
        }
        with(profile.bio){
            if (isBlank() || length > 150){
                return Result.Error(message = Constants.INVALID_INPUT_BIO_MESSAGE)
            }
        }
        return profileRepository.updateProfile(profile, imageBytes)
    }
}