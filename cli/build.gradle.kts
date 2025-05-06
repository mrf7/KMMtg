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
    implementation(project(":utils"))
    implementation(libs.bundles.base)
    implementation(libs.mosaic)
    implementation(libs.mosaic.animation)
    // gets rid of annoying logger thing missing, prob a better way but im not sure what lib is bringing in the dep on it
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation(libs.koin.compose)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
application {
    mainClass.set("CliKt")
}
