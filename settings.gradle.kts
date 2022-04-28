rootProject.name = "KMMtg"
include(":carddb")
include("scryfall")
include("discord-bot")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val touchlab = "co.touchlab"
            val kotlinx = "org.jetbrains.kotlinx"
            // base stuff
            version("kermit", "1.0.0")
            library("kermit", touchlab, "kermit").versionRef("kermit")

            version("coroutines", "1.6.1-native-mt")
            library("coroutines-core", kotlinx, "kotlinx-coroutines-core").versionRef("coroutines")
            bundle("base", listOf("kermit","coroutines-core"))

            // Web stuff
            version("ktor", "2.0.0")
            library("ktor-client-core", "io.ktor","ktor-client-core").versionRef("ktor")
            library("ktor-client-jvm", "io.ktor","ktor-client-cio").versionRef("ktor")
            library("ktor-client-okhttp", "io.ktor","ktor-client-okhttp").versionRef("ktor")
            library("ktor-client-ios", "io.ktor","ktor-client-ios").versionRef("ktor")
            library("ktor-client-js", "io.ktor","ktor-client-js").versionRef("ktor")
            library("ktor-kotlinx-serialization", "io.ktor", "ktor-serialization-kotlinx-json").versionRef("ktor")
            library("ktor-content-negotiation", "io.ktor","ktor-client-content-negotiation" ).versionRef("ktor")

            bundle("web", listOf("ktor-client-core", "ktor-kotlinx-serialization", "ktor-content-negotiation"))

            //database
            version("sqlDelight", "1.5.3")
            library("sqldelight-driver-jvm","com.squareup.sqldelight", "sqlite-driver").versionRef("sqlDelight")
            library("sqldelight-driver-android","com.squareup.sqldelight", "android-driver").versionRef("sqlDelight")
            library("sqldelight-driver-native","com.squareup.sqldelight", "native-driver").versionRef("sqlDelight")
            library("sqldelight-coroutines","com.squareup.sqldelight", "coroutines-extensions").versionRef("sqlDelight")

        }
    }
}