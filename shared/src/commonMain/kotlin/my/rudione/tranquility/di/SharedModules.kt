package my.rudione.tranquility.di

import my.rudione.tranquility.account.data.AccountApiService
import my.rudione.tranquility.account.data.repository.ProfileRepositoryImpl
import my.rudione.tranquility.account.domain.repository.ProfileRepository
import my.rudione.tranquility.account.domain.usecase.GetProfileUseCase
import my.rudione.tranquility.account.domain.usecase.UpdateProfileUseCase
import my.rudione.tranquility.auth.data.AuthRepositoryImpl
import my.rudione.tranquility.auth.data.AuthService
import my.rudione.tranquility.auth.domain.repository.AuthRepository
import my.rudione.tranquility.auth.domain.usecase.SignInUseCase
import my.rudione.tranquility.auth.domain.usecase.SignUpUseCase
import my.rudione.tranquility.common.data.remote.FollowsApiService
import my.rudione.tranquility.common.data.remote.PostApiService
import my.rudione.tranquility.common.util.provideDispatcher
import my.rudione.tranquility.follows.data.FollowsRepositoryImpl
import my.rudione.tranquility.follows.domain.FollowsRepository
import my.rudione.tranquility.follows.domain.usecase.FollowOrUnfollowUseCase
import my.rudione.tranquility.follows.domain.usecase.GetFollowableUsersUseCase
import my.rudione.tranquility.follows.domain.usecase.GetFollowsUseCase
import my.rudione.tranquility.post.data.PostRepositoryImpl
import my.rudione.tranquility.post.data.remote.PostCommentsApiService
import my.rudione.tranquility.post.data.repository.PostCommentsRepositoryImpl
import my.rudione.tranquility.post.domain.repository.PostCommentsRepository
import my.rudione.tranquility.post.domain.repository.PostRepository
import my.rudione.tranquility.post.domain.usecase.AddPostCommentUseCase
import my.rudione.tranquility.post.domain.usecase.CreatePostUseCase
import my.rudione.tranquility.post.domain.usecase.GetPostCommentsUseCase
import my.rudione.tranquility.post.domain.usecase.GetPostUseCase
import my.rudione.tranquility.post.domain.usecase.GetPostsUseCase
import my.rudione.tranquility.post.domain.usecase.GetUserPostsUseCase
import my.rudione.tranquility.post.domain.usecase.LikeOrDislikePostUseCase
import my.rudione.tranquility.post.domain.usecase.RemovePostCommentUseCase
import org.koin.dsl.module

private val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    factory { AuthService() }
    factory { SignUpUseCase() }
    factory { SignInUseCase() }
}

private val utilityModule = module {
    factory { provideDispatcher() }
}

private val postModule = module {
    factory { PostApiService() }
    factory { GetPostsUseCase() }
    factory { LikeOrDislikePostUseCase() }
    factory { GetUserPostsUseCase() }
    factory { GetPostUseCase() }
    factory { CreatePostUseCase() }

    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
}

private val postCommentModule = module {
    factory { PostCommentsApiService() }
    factory { GetPostCommentsUseCase() }
    factory { AddPostCommentUseCase() }
    factory { RemovePostCommentUseCase() }

    single<PostCommentsRepository> { PostCommentsRepositoryImpl(get(), get(), get()) }
}

private val followsModule = module {
    factory { FollowsApiService() }
    factory { GetFollowableUsersUseCase() }
    factory { FollowOrUnfollowUseCase() }
    factory { GetFollowsUseCase() }

    single<FollowsRepository> { FollowsRepositoryImpl(get(), get(), get()) }
}

private val accountModule = module {
    factory { AccountApiService() }
    factory { GetProfileUseCase() }
    factory { UpdateProfileUseCase() }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }
}

fun getSharedModules() = listOf(
    platformModule,
    authModule,
    utilityModule,
    postModule,
    followsModule,
    accountModule,
    postCommentModule
)