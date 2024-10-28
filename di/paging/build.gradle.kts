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
            implementation(project(":di:repository"))
            implementation(project(":models"))
            implementation(libs.koin.core)
            implementation(libs.decompose.base)
            api(libs.paging.common)
        }
    }
}