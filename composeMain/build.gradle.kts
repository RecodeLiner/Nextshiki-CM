plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.moko)
}

kotlin {
    androidTarget()
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(project(":components"))
            implementation(libs.windowSize)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.decompose.compose)
            implementation(libs.decompose.comp.experimtental)
            implementation(libs.bundles.coil)
            implementation(libs.materialKolor)
            implementation(libs.bundles.kmpalette)
            implementation(libs.rich.text)
            implementation(libs.bundles.moko)
        }
        jvmMain.dependencies {
            implementation(libs.accents)
            implementation(libs.ktor.client.okhttp)
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(compose.uiTooling)
            implementation(compose.preview)
        }
    }
}

android {
    namespace = "com.rcl.nextshiki"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
}