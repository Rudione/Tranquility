package my.rudione.tranquility.post.data.remote

import my.rudione.tranquility.common.data.remote.KtorApi
import my.rudione.tranquility.common.util.Constants
import my.rudione.tranquility.post.data.model.CommentApiResponse
import my.rudione.tranquility.post.data.model.GetPostCommentsApiResponse
import my.rudione.tranquility.post.data.model.NewCommentParams
import my.rudione.tranquility.post.data.model.RemoveCommentParams
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class PostCommentsApiService: KtorApi() {
    suspend fun getPostComments(
        userToken: String,
        postId: Long,
        page: Int,
        pageSize: Int
    ): GetPostCommentsApiResponse {
        val httpResponse = client.get {
            endPoint(path = "/post/comments/$postId")
            parameter(key = Constants.PAGE_QUERY_PARAMETER, page)
            parameter(key = Constants.PAGE_SIZE_QUERY_PARAMETER, pageSize)
            setToken(token = userToken)
        }
        return GetPostCommentsApiResponse(code = httpResponse.status, data = httpResponse.body())
    }

    suspend fun addComment(
        commentParams: NewCommentParams,
        userToken: String
    ): CommentApiResponse {
        val httpResponse = client.post {
            endPoint(path = "/post/comments/create")
            setBody(body = commentParams)
            setToken(token = userToken)
        }
        return CommentApiResponse(code = httpResponse.status, data = httpResponse.body())
    }

    suspend fun removeComment(
        commentParams: RemoveCommentParams,
        userToken: String
    ): CommentApiResponse{
        val httpResponse = client.delete {
            endPoint(path = "/post/comments/delete")
            setBody(body = commentParams)
            setToken(token = userToken)
        }
        return CommentApiResponse(code = httpResponse.status, data = httpResponse.body())
    }
}












