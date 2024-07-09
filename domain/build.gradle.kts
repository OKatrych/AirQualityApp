plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

dependencies {
    api(project.dependencies.platform(libs.koin.bom))
    api(libs.koin.core)
    api(libs.koin.annotations)

    api(libs.kotlin.serialization)
    api(libs.kotlin.datetime)
    api(libs.kotlin.coroutines)
    // Kermit
    api(libs.kermit)
    implementation(libs.compose.runtime)
}