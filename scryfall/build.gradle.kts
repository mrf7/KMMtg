import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
//    id("com.android.library") TODO This breaks for some reason
    alias(libs.plugins.kotlin.serialization)
}

group = "com.mfriend"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs{
        browser()
    }

    applyDefaultHierarchyTemplate()
    sourceSets {
        commonMain.dependencies {
            implementation(project(":utils"))
            implementation(libs.bundles.base)
            implementation(libs.bundles.web)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
//        val androidMain by getting {
//            dependencies {
//                implementation(libs.ktor.client.okhttp)
//            }
//        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

// android {
//    compileSdk = 30
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        minSdk = 21
//    }
// }
