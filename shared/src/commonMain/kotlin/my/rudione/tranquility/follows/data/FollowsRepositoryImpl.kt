package my.rudione.tranquility.follows.data

import my.rudione.tranquility.common.data.local.UserPreferences
import my.rudione.tranquility.common.data.model.FollowsParams
import my.rudione.tranquility.common.data.remote.FollowsApiService
import my.rudione.tranquility.common.domain.model.FollowsUser
import my.rudione.tranquility.common.util.Constants
import my.rudione.tranquility.common.util.DispatcherProvider
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.common.util.safeApiCall
import my.rudione.tranquility.follows.domain.FollowsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.withContext

internal class FollowsRepositoryImpl(
    private val followsApiService: FollowsApiService,
    private val userPreferences: UserPreferences,
    private val dispatcher: DispatcherProvider
): FollowsRepository{
    override suspend fun getFollowableUsers(): Result<List<FollowsUser>> {
        return withContext(dispatcher.io) {
            try {
                val userData = userPreferences.getUserData()
                val apiResponse = followsApiService.getFollowableUsers(userData.token, userData.id)

                when (apiResponse.code) {
                    HttpStatusCode.OK -> {
                        Result.Success(data = apiResponse.data.follows.map { it.toDomainFollowUser() })
                    }

                    HttpStatusCode.BadRequest -> {
                        Result.Error(message = "${apiResponse.data.message}")
                    }

                    HttpStatusCode.Forbidden -> {
                        Result.Success(data = emptyList())
                    }

                    else -> {
                        Result.Error(message = "${apiResponse.data.message}")
                    }
                }
            }catch (ioException: IOException) {
                Result.Error(message = Constants.NO_INTERNET_ERROR)
            }
        }
    }

    override suspend fun followOrUnfollow(
        followedUserId: Long,
        shouldFollow: Boolean
    ): Result<Boolean> {
        return withContext(dispatcher.io) {
            try {
                val userData = userPreferences.getUserData()
                val followParams = FollowsParams(userData.id, followedUserId)

                val apiResponse = if (shouldFollow) {
                    followsApiService.followUser(userData.token, followParams)
                } else {
                    followsApiService.unfollowUser(userData.token, followParams)
                }

                if (apiResponse.code == HttpStatusCode.OK) {
                    Result.Success(data = apiResponse.data.success)
                } else {
                    Result.Error(data = false, message = "${apiResponse.data.message}")
                }
            } catch (ioException: IOException) {
                Result.Error(message = Constants.NO_INTERNET_ERROR)
            } catch (exception: Throwable) {
                Result.Error(
                    message = "${exception.message}"
                )
            }
        }
    }

    override suspend fun getFollows(
        userId: Long,
        page: Int,
        pageSize: Int,
        followsType: Int
    ): Result<List<FollowsUser>> {
        return safeApiCall(dispatcher){
            val currentUserData = userPreferences.getUserData()
            val apiResponse = followsApiService.getFollows(
                userToken = currentUserData.token,
                userId = userId,
                page = page,
                pageSize = pageSize,
                followsEndPoint = if (followsType == 1) "followers" else "following"
            )


            if (apiResponse.code == HttpStatusCode.OK){
                Result.Success(data = apiResponse.data.follows.map { it.toDomainFollowUser() })
            }else{
                Result.Error(message = "${apiResponse.data.message}")
            }
        }
    }
}















