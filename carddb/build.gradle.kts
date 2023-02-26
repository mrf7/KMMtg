plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
//    id("com.android.library")
    id("com.squareup.sqldelight")
    id("io.realm.kotlin") version "1.6.0"
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    jvm()
    ios()
    sourceSets {
        /* Main source sets */
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.base)
                implementation(libs.sqldelight.coroutines)
                implementation("io.realm.kotlin:library-base:1.6.0")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.jvm)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.native)
            }
        }

        // Test sets
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
}

sqldelight {
    database("MTGDb") {
        packageName = "com.mfriend.db"
    }
}
//android {
//    compileSdk = 30
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        minSdk = 21
//    }
//}