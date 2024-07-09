plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
}

android {
    namespace = "dev.olek.payback"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.olek.payback"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionName = libs.versions.project.get()
        versionCode = libs.versions.versionCode.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    kotlinOptions {
        val configPath = "$rootDir/compose_compiler_config.conf"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=$configPath"
        )
        val composeReportsDir = "${buildDir}/composeReports"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$composeReportsDir"
        ) + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$composeReportsDir"
        )
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.configureEach {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.compose.activity)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.lifecycle)
    implementation(libs.compose.navigation)
    implementation(libs.coil)
    implementation(libs.kotlin.coroutines.play)

    debugImplementation(libs.compose.ui.tooling)
    ksp(libs.koin.ksp.compiler)
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
    arg("KOIN_DEFAULT_MODULE","false")
}