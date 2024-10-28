
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.moko)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget {
        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
                    freeCompilerArgs.add("-Xjdk-release=${libs.versions.java.get()}")
                }
            }
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)
            dependencies {
                debugImplementation(libs.leak.canary)
            }
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.decompose.base)
            export(libs.moko.base)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":components"))
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.bundles.decompose)
            implementation(libs.napier)
            implementation(libs.koin.core)
            implementation(libs.paging.compose.common)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.koin.test)
        }

        androidMain.dependencies {
            implementation(project(":composeMain"))
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.uitooling)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
            implementation(compose.material3)
        }

        jvmMain.dependencies {
            implementation(project(":composeMain"))
            implementation(libs.accents)
            implementation(compose.desktop.common)
            implementation(compose.materialIconsExtended)
            implementation(libs.kotlinx.coroutines.swing)
        }

        iosMain.dependencies {
            implementation(project(":components"))
            implementation(libs.ktor.client.darwin)
        }
    }
}

composeCompiler {
    targetKotlinPlatforms.set(
        KotlinPlatformType.values()
            .filterNot { it == KotlinPlatformType.native || it == KotlinPlatformType.js }
            .asIterable()
    )
}

android {
    namespace = "com.rcl.nextshiki"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        applicationId = "com.rcl.nextshiki.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
    buildTypes {
        release {
            isDefault = true
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    bundle {
        language {
            enableSplit = true
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Deb, TargetFormat.Rpm, TargetFormat.AppImage,
                TargetFormat.Exe, TargetFormat.Msi
            )
            packageName = rootProject.name
            packageVersion = "1.0.0"

            val pathToIcon = project.file("cfgs/icons")

            macOS {
                iconFile.set(pathToIcon.resolve("icon.icns"))
            }
            windows {
                iconFile.set(pathToIcon.resolve("icon.ico"))
            }
            linux {
                iconFile.set(pathToIcon.resolve("icon.png"))
            }
        }

        buildTypes.release {
            proguard {
                configurationFiles.from("compose.desktop.pro")
            }
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}