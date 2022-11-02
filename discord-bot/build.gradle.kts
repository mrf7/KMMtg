plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"


dependencies {
    implementation(project(":scryfall"))
    implementation(project(":carddb"))
    implementation(libs.arrow)
}