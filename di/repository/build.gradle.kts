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
            implementation(project(":resources"))
            implementation(project(":models"))
            implementation(project(":utils"))
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.decompose.base)
            implementation(libs.koin.core)
            implementation(libs.bundles.ktor)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}