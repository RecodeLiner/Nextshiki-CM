plugins {
    alias(libs.plugins.multiplatform)
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":di:settings"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}