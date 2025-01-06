plugins {
    kotlin("multiplatform")
    id("com.android.library")

    // Kotlinx Serialization
    kotlin("plugin.serialization") version "1.9.10"
}

kotlin {
    android()

    // Define the iOS targets
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // dependencies versions
    val coroutinesVersion = "1.6.4"
    val koinVersion = "3.3.2"
    val ktorVersion = "2.2.1"
    val datastoreVersion = "1.1.1"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                api("io.insert-koin:koin-core:$koinVersion")

                implementation("androidx.datastore:datastore-preferences-core:$datastoreVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                api("io.insert-koin:koin-android:$koinVersion")
                implementation("io.ktor:ktor-client-android:$ktorVersion")

                api("androidx.datastore:datastore-preferences:$datastoreVersion")
            }
        }

        val androidUnitTest by getting

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}

android {
    namespace = "my.rudione.tranquility"
    compileSdk = 34
    defaultConfig {
        minSdk = 28
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}