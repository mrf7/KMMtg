rootProject.name = "KMMtg"
include(":carddb")
include("scryfall")
include("discord-bot")
include(":collection-import")
include(":cli")
include(":app")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}
