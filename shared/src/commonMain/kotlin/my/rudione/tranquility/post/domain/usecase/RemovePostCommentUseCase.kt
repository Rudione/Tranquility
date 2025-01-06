package my.rudione.tranquility.post.domain.usecase

import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.post.domain.model.PostComment
import my.rudione.tranquility.post.domain.repository.PostCommentsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemovePostCommentUseCase: KoinComponent {
    private val repository: PostCommentsRepository by inject()

    suspend operator fun invoke(postId: Long, commentId: Long): Result<PostComment?> {
        return repository.removeComment(postId = postId, commentId = commentId)
    }
}