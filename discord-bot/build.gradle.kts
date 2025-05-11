plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":scryfall"))
    implementation(project(":carddb"))
    implementation(project(":utils"))
    implementation(libs.bundles.base)
    implementation(libs.arrow.optics)
    implementation(libs.arrow.fx.coroutines)
    ksp(libs.arrow.optics.ksp)
}
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
