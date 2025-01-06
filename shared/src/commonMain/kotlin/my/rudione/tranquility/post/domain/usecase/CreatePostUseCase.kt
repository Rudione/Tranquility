package my.rudione.tranquility.post.domain.usecase

import my.rudione.tranquility.common.domain.model.Post
import my.rudione.tranquility.common.util.Constants
import my.rudione.tranquility.common.util.Result
import my.rudione.tranquility.post.domain.repository.PostRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreatePostUseCase: KoinComponent {
    private val repository by inject<PostRepository>()

    suspend operator fun invoke(
        caption: String,
        imageBytes: ByteArray
    ): Result<Post>{
        with(caption){
            if (isBlank() || length > 200){
                return Result.Error(message = Constants.INVALID_INPUT_POST_CAPTION_MESSAGE)
            }
        }
        return repository.createPost(caption = caption, imageBytes = imageBytes)
    }
}