plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "com.mfriend"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":scryfall"))
                implementation(libs.bundles.base)
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val iosMain by getting {
            dependencies {
            }
        }
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
}

