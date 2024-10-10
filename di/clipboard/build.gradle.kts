plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.multiplatform)
}

kotlin {
    androidTarget()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
    }
}

android {
    namespace = "com.rcl.nextshiki"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
}