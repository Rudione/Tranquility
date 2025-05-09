package my.rudione.tranquility.android

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import my.rudione.editprofile.navigation.EditProfileNavigation
import my.rudione.home.navigation.HomeNavigation
import my.rudione.login.navigation.LoginNavigation
import my.rudione.post.navigation.PostDetailNavigation
import my.rudione.signup.navigation.SignUpNavigation
import my.rudione.tranquility.android.di.appModule
import my.rudione.tranquility.di.getSharedModules
import my.rudione.ui.SharedScreen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TranquilityApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TranquilityApplication)
            modules(appModule + getSharedModules())
        }

        ScreenRegistry {
            register<SharedScreen.SignUpScreen> { SignUpNavigation() }
            register<SharedScreen.LoginScreen> { LoginNavigation() }
            register<SharedScreen.HomeScreen> { HomeNavigation() }
            register<SharedScreen.PostDetailScreen> { provider ->
                PostDetailNavigation(postId = provider.id)
            }
            register<SharedScreen.ProfileScreen> { provider ->
                PostDetailNavigation(postId = provider.id)
            }
            register<SharedScreen.EditProfileScreen> { provider ->
                EditProfileNavigation(userId = provider.id)
            }
            register<SharedScreen.FollowsScreen> { provider ->
                EditProfileNavigation(userId = provider.id)
            }
            register<SharedScreen.FollowingScreen> { provider ->
                EditProfileNavigation(userId = provider.id)
            }
        }
    }
}