plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
//    id("com.android.library")
    alias(libs.plugins.sqlDelight)
}

kotlin {
    /* Targets configuration omitted.
     *  To find out how to configure the targets, please follow the link:
     *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    applyDefaultHierarchyTemplate()

    sourceSets {
        /* Main source sets */
        commonMain.dependencies {
            implementation(project(":utils"))
            implementation(libs.bundles.base)
            implementation(libs.sqldelight.coroutines)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.driver.jvm)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.driver.native)
        }

        // Test sets
        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

sqldelight {
    databases.create("MTGDb") {
        packageName.set("com.mfriend.db")
    }
}
// android {
//    compileSdk = 30
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        minSdk = 21
//    }
// }
