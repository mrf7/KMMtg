plugins {
    kotlin("multiplatform")
//    id("com.android.library") TODO This breaks for some reason
    kotlin("plugin.serialization")
}

group = "com.mfriend"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()
    ios()
//    android()
    js(IR) {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.base)
                implementation(libs.bundles.web)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.jvm)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.sqldelight.driver)
            }
        }
//        val androidMain by getting {
//            dependencies {
//                implementation(libs.ktor.client.okhttp)
//            }
//        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
}

//android {
//    compileSdk = 30
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        minSdk = 21
//    }
//}
