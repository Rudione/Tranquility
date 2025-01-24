buildscript {
    val agp_version by extra("8.1.1")
}

plugins {
    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.composeCompiler).apply(false)
    alias(libs.plugins.jetbrains.kotlin.jvm).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
}
val sourceCompatibility by extra("17")
val targetCompatibility by extra(sourceCompatibility)
val sourceCompatibility1 by extra(sourceCompatibility)

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("kspDebugKotlin") {
    group = "build"
    description = "Compile KSP sources for debug"
    dependsOn(":feature:login:kspDebugKotlin")
    dependsOn(":feature:signup:kspDebugKotlin")
    dependsOn(":feature:home:kspDebugKotlin")
    dependsOn(":androidApp:kspDebugKotlin")
}