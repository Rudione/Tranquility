package my.rudione.tranquility.post.domain.usecase

import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.post.domain.repository.PostRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LikeOrDislikePostUseCase : KoinComponent {
    private val repository by inject<PostRepository>()

    suspend operator fun invoke(post: Post): Result<Boolean> {
        return repository.likeOrDislikePost(post.postId, !post.isLiked)
    }
}