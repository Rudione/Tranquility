package my.rudione.tranquility.android

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import my.rudione.home.navigation.HomeNavigation
import my.rudione.login.navigation.LoginNavigation
import my.rudione.signup.navigation.SignUpNavigation
import my.rudione.tranquility.android.di.appModule
import my.rudione.tranquility.auth.di.getSharedModules
import my.rudione.ui.SharedScreen
import org.koin.core.context.startKoin

class TranquilityApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule + getSharedModules())
        }
        ScreenRegistry {
            register<SharedScreen.SignUpScreen> {
                SignUpNavigation()
            }
            register<SharedScreen.LoginScreen> {
                LoginNavigation()
            }
            register<SharedScreen.HomeScreen> {
                HomeNavigation()
            }
        }
    }
}