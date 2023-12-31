import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.moko.multiplatform.resources)
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

if (project.rootProject.file("local.properties").exists()){
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
} else{
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
                jvmTarget = "1.8"
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
            export(libs.moko)
            // export(libs.decompose.base)
            export(libs.essenty)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.windowSize)
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(libs.essenty)
                //implementation(libs.bundles.decompose)
                implementation(libs.compose.rich.text)
                implementation(compose.materialIconsExtended)
                implementation(libs.bundles.voyager)
                implementation(libs.composeImageLoader)
                implementation(libs.napier)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.bundles.ktor)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.bundles.multiplatformSettings)
                implementation(libs.bundles.koin)
                implementation(libs.kstore)
                implementation(libs.bundles.moko)
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
            }
        }

        val desktopMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(compose.uiTooling)
                implementation(compose.preview)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

            macOS {
                iconFile.set(project.file("src/icons/icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/icons/icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/icons/icon.png"))
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
    multiplatformResourcesPackage = "com.rcl.nextshiki"
}