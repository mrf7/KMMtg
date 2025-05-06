plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.mfriend"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()
    // TODO Csv reader currenetly only supports jvm and js. Might be able to add support for ios manually using okio
    // ios()
    // wasmJs
    sourceSets {
        commonMain.dependencies {
            implementation(project(":scryfall"))
            implementation(project(":utils"))
            implementation(libs.bundles.base)
            implementation("com.github.doyaaaaaken:kotlin-csv:1.6.0")
        }
        jvmMain.dependencies {
        }
//        val iosMain by getting {
//            dependencies {
//            }
//        }
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }
    }
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
