import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.plugin.KspExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // These are typically defined in libs.versions.toml and referenced via alias
        // For direct definition, replace with actual versions if not using toml
        // classpath "com.android.tools.build:gradle:8.2.0" // Example version
        // classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0" // Example version
        // classpath "com.google.dagger:hilt-android-gradle-plugin:2.48" // Example version
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Define versions explicitly if not using libs.versions.toml for this example
ext {
    set("androidCompileSdk", 34)
    set("androidMinSdk", 24)
    set("androidTargetSdk", 34)
    set("androidxComposeBom", "2023.08.00") // Or latest stable
    set("androidxComposeCompiler", "1.5.1") // Match your Kotlin version, e.g., for Kotlin 1.9.0
    set("kotlinJvmTarget", "17")
    set("hiltVersion", "2.48")
    set("roomVersion", "2.6.0")
    set("lifecycleVersion", "2.6.2")
    set("activityComposeVersion", "1.8.1")
    set("navigationComposeVersion", "2.7.5")
    set("coroutinesVersion", "1.7.3")
    set("timberVersion", "5.0.1")
    set("playServicesAdsVersion", "22.4.0")
}

// Configuration for common Android settings
fun configureAndroid(commonExtension: com.android.build.api.dsl.CommonExtension<*, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = (rootProject.extra["androidCompileSdk"] as Int)

        defaultConfig {
            minSdk = (rootProject.extra["androidMinSdk"] as Int)
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions {
            jvmTarget = rootProject.extra["kotlinJvmTarget"] as String
        }

        buildFeatures {
            buildConfig = true
        }
    }
}

// Configuration for Compose
fun configureCompose(commonExtension: com.android.build.api.dsl.CommonExtension<*, *, *, *, *>) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = rootProject.extra["androidxComposeCompiler"] as String
        }
    }
}

// Configuration for KSP
fun configureKsp(kspExtension: KspExtension) {
    kspExtension.arg("room.schemaLocation", "$projectDir/schemas")
}