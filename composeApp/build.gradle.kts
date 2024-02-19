
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.moko.plugin)
}

var redirectURI: String = ""
var clientId: String = ""
var clientSecret: String = ""
var domain: String = ""
var scope: String = ""
var userAgent: String = ""
var clientIDDesk: String = ""
var clientSecretDesk: String = ""
var redirectURIDesk: String = ""
var scopeDesk: String = ""
var userAgentDesk: String = ""
val isMetricsEnabled: Boolean = true

if (project.rootProject.file("local.properties").exists()) {
    redirectURI = gradleLocalProperties(rootDir).getProperty("redirectURI")
    clientId = gradleLocalProperties(rootDir).getProperty("clientId")
    clientSecret = gradleLocalProperties(rootDir).getProperty("clientSecret")
    domain = gradleLocalProperties(rootDir).getProperty("domain")
    userAgent = gradleLocalProperties(rootDir).getProperty("userAgent")
    clientIDDesk = gradleLocalProperties(rootDir).getProperty("clientIDDesk")
    clientSecretDesk = gradleLocalProperties(rootDir).getProperty("clientSecretDesk")
    redirectURIDesk = gradleLocalProperties(rootDir).getProperty("redirectURIDesk")
    scope = gradleLocalProperties(rootDir).getProperty("scope")
    scopeDesk = gradleLocalProperties(rootDir).getProperty("scopeDesk")
    userAgentDesk = gradleLocalProperties(rootDir).getProperty("userAgentDesk")
} else {
    redirectURI = System.getenv("redirectURI")
    clientId = System.getenv("clientId")
    clientSecret = System.getenv("clientSecret")
    domain = System.getenv("domain")
    userAgent = System.getenv("userAgent")
    clientIDDesk = System.getenv("clientIDDesk")
    clientSecretDesk = System.getenv("clientSecretDesk")
    redirectURIDesk = System.getenv("redirectURIDesk")
    scope = System.getenv("scope")
    scopeDesk = System.getenv("scopeDesk")
    userAgentDesk = System.getenv("userAgentDesk")
}


kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Compose application framework"
        homepage = "empty"
        ios.deploymentTarget = "11.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.moko.resources)
            export(libs.decompose.base)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.flexible.bottom)
                implementation(libs.windowSize)
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(libs.bundles.decompose)
                implementation(compose.materialIconsExtended)
                implementation(libs.napier)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.bundles.ktor)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.bundles.multiplatformSettings)
                implementation(libs.koin.core)
                implementation(libs.kamel)
                implementation(libs.kstore)
                implementation(libs.materialKolor)
                implementation(libs.bundles.kmpalette)
                implementation(libs.moko.resources)
                implementation(libs.moko.compose)
                implementation(libs.moko.compose)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.androidx.activityCompose)
                implementation(libs.compose.uitooling)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.androidx.appcompat)
                implementation(libs.koin.android)
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.desktopAccent)
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(compose.uiTooling)
                implementation(compose.preview)
                implementation(libs.bundles.jewel)
            }
        }

        val iosMain by getting {
            dependsOn(commonMain)
            dependencies {

            }
        }
    }
}

android {
    namespace = "com.rcl.nextshiki"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "com.rcl.nextshiki.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        create("releaseWithLogger") {
            isDebuggable = true
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isJniDebuggable = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    @Suppress("UnstableApiUsage")
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
            packageName = "com.rcl.nextshiki.desktopApp"
            packageVersion = "1.0.0"

            val pathToIcon = project.file("src/icons")

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

buildConfig {
    buildConfigField("String", "REDIRECT_URI", redirectURI)
    buildConfigField("String", "CLIENT_ID", clientId)
    buildConfigField("String", "CLIENT_SECRET", clientSecret)
    buildConfigField("String", "DOMAIN", domain)
    buildConfigField("String", "USER_AGENT", userAgent)
    buildConfigField("String", "CLIENT_ID_DESK", clientIDDesk)
    buildConfigField("String", "CLIENT_SECRET_DESK", clientSecretDesk)
    buildConfigField("String", "REDIRECT_URI_DESK", redirectURIDesk)
    buildConfigField("String", "SCOPE", scope)
    buildConfigField("String", "SCOPE_DESK", scopeDesk)
    buildConfigField("String", "USER_AGENT_DESK", userAgentDesk)
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

multiplatformResources {
    resourcesPackage.set("com.rcl.moko")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        if (isMetricsEnabled) {
            freeCompilerArgs += "-P"
            freeCompilerArgs += "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + project.projectDir.path + "/compose_metrics"
            freeCompilerArgs += "-P"
            freeCompilerArgs += "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + project.projectDir.path + "/compose_metrics"
        }
    }
}

//Thanks google for broken agp with Lint
afterEvaluate {
    tasks.named("generateDebugLintReportModel").configure {
        mustRunAfter("generateAndroidUnitTestDebugNonAndroidBuildConfig")
        mustRunAfter("generateAndroidUnitTestNonAndroidBuildConfig")
    }
    tasks.named("lintAnalyzeDebug").configure {
        mustRunAfter("generateAndroidUnitTestDebugNonAndroidBuildConfig")
        mustRunAfter("generateAndroidUnitTestNonAndroidBuildConfig")
    }
}