plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "br.com.controlefamiliar"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.controlefamiliar"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
}
