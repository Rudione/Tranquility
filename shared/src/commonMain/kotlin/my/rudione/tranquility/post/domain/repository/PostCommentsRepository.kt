package my.rudione.tranquility.post.domain.repository

import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.post.domain.model.PostComment

internal interface PostCommentsRepository {
    suspend fun getPostComments(postId: Long, page: Int, pageSize: Int): Result<List<PostComment>>
    suspend fun addComment(postId: Long, content: String): Result<PostComment>
    suspend fun removeComment(postId: Long, commentId: Long): Result<PostComment?>
}