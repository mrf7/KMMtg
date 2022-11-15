plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

group = "com.mfriend"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()
    ios()
//    android()
   js(IR) {
       nodejs()
       binaries.executable()
   }

    sourceSets {
        val commonMain by getting {
            dependencies {
//                implementation(project(":carddb"))
//                implementation(project(":scryfall"))
//                implementation(libs.bundles.base)
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
//        val androidMain by getting {
//            dependencies {
//            }
//        }
        val iosMain by getting {
            dependencies {
            }
        }
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
}

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
}
