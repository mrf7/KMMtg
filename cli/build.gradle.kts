plugins {
    kotlin("jvm")
    id("application")
    alias(libs.plugins.compose.compiler)
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":scryfall"))
    implementation(project(":carddb"))
    implementation(project(":collection-import"))
    implementation(libs.bundles.base)
    implementation(libs.mosaic)
    implementation(libs.koin.compose)
    implementation("org.jline:jline:3.22.0")
}

application {
    mainClass.set("CliKt")
}
