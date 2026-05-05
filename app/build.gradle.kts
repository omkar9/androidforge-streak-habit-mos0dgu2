plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ksp")
    id("hilt")
}

android {
    namespace = "com.androidforge.streakhappit"
    configureAndroid(this)
    configureCompose(this)

    defaultConfig {
        applicationId = "com.androidforge.streakhappit"
        targetSdk = (rootProject.extra["androidTargetSdk"] as Int)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ksp {
    configureKsp(this)
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // AdMob
    implementation(libs.play.services.ads)

    // Timber for logging
    implementation(libs.timber)

    // Project Modules
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}

// libs.versions.toml content (for reference, these are usually in a separate file)
// [versions]
// android-application = "8.2.0"
// android-library = "8.2.0"
// kotlin-android = "1.9.0"
// kotlin-jvm = "1.9.0"
// ksp = "1.9.0-1.0.13"
// hilt = "2.48"
// androidx-core-ktx = "1.12.0"
// androidx-lifecycle-runtime-ktx = "2.6.2"
// androidx-activity-compose = "1.8.1"
// androidx-compose-bom = "2023.08.00"
// androidx-compose-compiler = "1.5.1"
// androidx-material3 = "1.1.2"
// androidx-navigation-compose = "2.7.5"
// androidx-hilt-navigation-compose = "1.1.0"
// androidx-room = "2.6.0"
// kotlinx-coroutines = "1.7.3"
// timber = "5.0.1"
// play-services-ads = "22.4.0"
// junit = "4.13.2"
// androidx-junit = "1.1.5"
// androidx-espresso-core = "3.5.1"

// [libraries]
// plugins-android-application = { id = "com.android.application", version.ref = "android-application" }
// plugins-android-library = { id = "com.android.library", version.ref = "android-library" }
// plugins-kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin-android" }
// plugins-kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin-jvm" }
// plugins-ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
// plugins-hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }

// androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "androidx-core-ktx" }
// androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "androidx-lifecycle-runtime-ktx" }
// androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "androidx-activity-compose" }
// androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "androidx-compose-bom" }
// androidx-ui = { group = "androidx.compose.ui", name = "ui" }
// androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
// androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
// androidx-material3 = { group = "androidx.compose.material3", name = "material3" }
// androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
// androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }

// androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "androidx-navigation-compose" }
// androidx-hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "androidx-hilt-navigation-compose" }

// hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
// hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }

// androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "androidx-room" }
// androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "androidx-room" }
// androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "androidx-room" }

// kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }
// kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "kotlinx-coroutines" }

// timber = { group = "com.jakewharton.timber", name = "timber", version.ref = "timber" }

// play-services-ads = { group = "com.google.android.gms", name = "play-services-ads", version.ref = "play-services-ads" }

// junit = { group = "junit", name = "junit", version.ref = "junit" }
// androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "androidx-junit" }
// androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "androidx-espresso-core" }
// androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
