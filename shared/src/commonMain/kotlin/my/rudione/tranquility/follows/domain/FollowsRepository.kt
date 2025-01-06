package my.rudione.tranquility.follows.domain

import my.rudione.tranquility.common.domain.model.FollowsUser
import my.rudione.tranquility.common.util.Result

interface FollowsRepository {
    suspend fun getFollowableUsers(): Result<List<FollowsUser>>
    suspend fun followOrUnfollow(followedUserId: Long, shouldFollow: Boolean): Result<Boolean>
    suspend fun getFollows(userId: Long, page: Int, pageSize: Int, followsType: Int): Result<List<FollowsUser>>
}