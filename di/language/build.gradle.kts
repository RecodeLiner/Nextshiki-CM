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
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.decompose.base)
            implementation(project(":di:settings"))
        }
    }
}