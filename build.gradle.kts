// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

// Add buildscript block for Firebase
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Latest version of Google Services plugin
        classpath("com.google.gms:google-services:4.4.1")
    }
}
