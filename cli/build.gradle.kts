plugins {
    kotlin("jvm")
    id("com.jakewharton.mosaic")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"


dependencies {
    implementation(project(":scryfall"))
    implementation(project(":carddb"))
    implementation(libs.bundles.base)
    implementation("org.jline:jline:3.22.0")
}

application {
    mainClass.set("CliKt")
}