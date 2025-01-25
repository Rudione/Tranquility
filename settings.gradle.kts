enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Tranquility"
include(":androidApp")
include(":shared")

include(":feature:home")

include(":core:designsystem") // theming, icons etc
include(":core:common") // utils
include(":core:ui") // components
include(":core:data") // dummy data / fake data
include(":core:model") // data models
include(":feature:login")
include(":feature:signup")
include(":feature:post")
include(":feature:profile")
include(":feature:editprofile")
include(":feature:follows")
include(":feature:createpost")
