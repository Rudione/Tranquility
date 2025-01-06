package my.rudione.tranquility.post.domain.usecase

import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.post.domain.model.PostComment
import my.rudione.tranquility.post.domain.repository.PostCommentsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPostCommentUseCase: KoinComponent {
    private val repository: PostCommentsRepository by inject()

    suspend operator fun invoke(postId: Long, content: String): Result<PostComment> {
        if (content.isBlank()){
            return Result.Error(message = "Comment content cannot be empty")
        }
        return repository.addComment(postId = postId, content = content)
    }
}