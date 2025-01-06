package my.rudione.tranquility.account.domain.repository

import my.rudione.tranquility.account.domain.model.Profile
import my.rudione.tranquility.common.util.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(profileId: Long): Flow<Result<Profile>>
    suspend fun updateProfile(profile: Profile, imageBytes: ByteArray?): Result<Profile>
}