plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "my.rudione.tranquility.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "my.rudione.tranquility.android"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    applicationVariants.all {
        addJavaSourceFoldersToModel(
            File(buildFile, "generated/ksp/$name/kotlin")
        )
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material)
    implementation(project(":feature:post"))
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.screenModel)
    implementation(libs.voyager.koin)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.coil.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(project(":core:designsystem"))
    implementation(project(":feature:signup"))
    implementation(project(":feature:login"))
    implementation(project(":feature:home"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
}