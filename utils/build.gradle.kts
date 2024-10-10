plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    androidTarget()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":models"))
            }
        }
        val mobileMain by creating {
            dependsOn(commonMain)
        }

        androidMain {
            dependsOn(mobileMain)
        }
        iosMain {
            dependsOn(mobileMain)
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