plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.sqlDelight) apply false
}

group = "org.example"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}