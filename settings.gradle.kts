rootProject.name = "KMMtg"
include(":carddb")
include("scryfall")
include("discord-bot")
include(":collection-import")
include(":cli")

pluginManagement {
    repositories {
        gradlePluginPortal()
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
