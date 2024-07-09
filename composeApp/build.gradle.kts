import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import java.util.Properties

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hierarchy)
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.moko)
    alias(libs.plugins.detekt)
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

if (project.rootProject.file("nextshikiAuth.properties").exists()) {
    val propertiesRead = Properties()
    propertiesRead.load(project.rootProject.file("nextshikiAuth.properties").inputStream())
    redirectURI = propertiesRead.getProperty("redirectURI")
    clientId = propertiesRead.getProperty("clientId")
    clientSecret = propertiesRead.getProperty("clientSecret")
    domain = propertiesRead.getProperty("domain")
    userAgent = propertiesRead.getProperty("userAgent")
    clientIDDesk = propertiesRead.getProperty("clientIDDesk")
    clientSecretDesk = propertiesRead.getProperty("clientSecretDesk")
    redirectURIDesk = propertiesRead.getProperty("redirectURIDesk")
    scope = propertiesRead.getProperty("scope")
    scopeDesk = propertiesRead.getProperty("scopeDesk")
    userAgentDesk = propertiesRead.getProperty("userAgentDesk")
    propertiesRead.clear()
    propertiesRead.load(project.rootProject.file("local.properties").inputStream())
    propertiesRead.clear()
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
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.flexible.bottom)
            implementation(libs.immutable.collections)
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
            implementation(libs.bundles.multiplatform.settings)
            implementation(libs.koin.core)
            implementation(libs.bundles.coil)
            implementation(libs.materialKolor)
            implementation(libs.bundles.kmpalette)
            implementation(libs.rich.text)
            implementation(libs.bundles.moko)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.koin.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.uitooling)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.appcompat)
            implementation(libs.koin.android)
        }

        jvmMain.dependencies {
            implementation(libs.accents)
            implementation(libs.ktor.client.okhttp)
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.uiTooling)
            implementation(compose.preview)
        }


        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
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
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
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
    }
    @Suppress("UnstableApiUsage")
    bundle {
        language {
            enableSplit = true
        }
    }
}

val desktopPackageName = "com.rcl.nextshiki.desktopApp"

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

dependencies {
    detektPlugins(libs.detekt.vkompose)
}

multiplatformResources {
    resourcesPackage.set("com.rcl.mr")
    resourcesClassName.set("SharedRes")
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
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = true
    config.setFrom("$projectDir/cfgs/config.yml")
}

tasks.withType<Detekt> {
    setSource(files(project.projectDir))
    exclude("**/build/**")
    exclude {
        it.file.relativeTo(projectDir).startsWith("build")
    }
}

tasks.register("detektAll") {
    allprojects {
        this@register.dependsOn(tasks.withType<Detekt>())
    }
}
