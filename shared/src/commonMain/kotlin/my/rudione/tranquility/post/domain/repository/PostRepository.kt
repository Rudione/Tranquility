package my.rudione.tranquility.post.domain.repository

import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.common.util.Result

interface PostRepository {
    suspend fun getFeedPosts(page: Int, pageSize: Int): Result<List<Post>>
    suspend fun likeOrDislikePost(postId: Long, shouldLike: Boolean): Result<Boolean>

    suspend fun getUserPosts(userId: Long, page: Int, pageSize: Int): Result<List<Post>>
    suspend fun getPost(postId: Long): Result<Post>
    suspend fun createPost(caption: String, imageBytes: ByteArray): Result<Post>
}