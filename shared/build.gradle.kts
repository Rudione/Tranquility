plugins {
    kotlin("multiplatform")
    id("com.android.library")

    // Kotlinx Serialization
    kotlin("plugin.serialization") version "1.9.10"
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    //Dependencies versions
    val coroutinesVersion = "1.6.4"
    val ktorVersion = "2.2.1"
    val koinVersion = "3.3.2"
    val datastoreVersion = "1.1.1"

    sourceSets {
        //commonMain sourceSet
        val commonMain by getting {
            //Common dependencies
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                api("io.insert-koin:koin-core:$koinVersion")

                implementation("androidx.datastore:datastore-preferences-core:$datastoreVersion")
                // androidx.datastore:datastore-preferences
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
            }
        }


        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        //AndroidMain sourceSet
        val androidMain by getting {
            dependencies {
                api("io.insert-koin:koin-android:$koinVersion")
                implementation("io.ktor:ktor-client-android:$ktorVersion")

                api("androidx.datastore:datastore-preferences:$datastoreVersion")
            }
        }

        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        //iOSMain sourceSet
        val iosMain by creating {
            dependencies{
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }

            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
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
dependencies {
    implementation(libs.androidx.datastore.core.android)
}
