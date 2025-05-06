plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":scryfall"))
    implementation(project(":carddb"))
    implementation(project(":utils"))
    implementation(libs.bundles.base)
}
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
