plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()
    sourceSets {
        commonMain.dependencies {
            api(project(":di:repository"))
            api(project(":di:clipboard"))
            api(project(":di:settings"))
            api(project(":di:language"))
            api(project(":resources"))
            api(project(":models"))
            api(project(":utils"))
            api(libs.moko.base)
            api(libs.kotlinx.datetime)
            api(project.dependencies.platform(libs.koin.bom))
            api(libs.decompose.base)
            api(libs.koin.core)
            api(libs.kotlinx.coroutines.core)
            api(libs.napier)
        }
    }
}