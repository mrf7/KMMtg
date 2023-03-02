plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "com.mfriend"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()
    // TODO Csv reader currenetly only supports jvm and js. Might be able to add support for ios manually using okio
//    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":scryfall"))
                implementation(libs.bundles.base)
                implementation("com.github.doyaaaaaken:kotlin-csv:1.6.0")
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
//        val iosMain by getting {
//            dependencies {
//            }
//        }
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
}

