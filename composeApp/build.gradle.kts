import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hierarchy)
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.moko)
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
var isMetricsEnabled: Boolean = true

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
    if (propertiesRead.getProperty("isMetricsEnabled") != null) {
        isMetricsEnabled = propertiesRead.getProperty("isMetricsEnabled").toBoolean()
    }
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

            val pathToIcon = project.file("icons")

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

multiplatformResources {
    resourcesPackage.set("com.rcl.mr")
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

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        if (isMetricsEnabled) {
            val options = listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + project.projectDir.path + "/compose_metrics", "-P", "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + project.projectDir.path + "/compose_metrics")
            freeCompilerArgs.addAll(options)
        }
    }
}

val appId = "com.rcl.nextshiki"
tasks.register("packageFlatpak") {
    group = "compose desktop"
    dependsOn("packageAppImage")
    doLast {
        delete {
            delete("$projectDir/build/flatpak/bin")
            delete("$projectDir/build/flatpak/lib")
        }
        copy {
            from("$projectDir/build/compose/binaries/main/app/Nextshiki/")
            into("$projectDir/build/flatpak/")
            exclude("$projectDir/build/compose/binaries/main/app/MyApp/lib/runtime/legal")
        }
        copy {
            from("$rootDir/composeApp/src/desktopMain/resources/flatpak/logo_round_preview.svg")
            into("$projectDir/build/flatpak/")
        }
        copy {
            from("$rootDir/composeApp/src/desktopMain/resources/flatpak/manifest.yml")
            into("$projectDir/build/flatpak/")
            rename {
                "$appId.yml"
            }
        }
        copy {
            from("$rootDir/composeApp/src/desktopMain/resources/flatpak/icon.desktop")
            into("$projectDir/build/flatpak/")
            rename {
                "$appId.desktop"
            }
        }
        exec {
            workingDir("$projectDir/build/flatpak/")
            commandLine(
                "flatpak-builder --install --user --force-clean --state-dir=build/flatpak-builder --repo=build/flatpak-repo build/flatpak-target $appId.yml".split(
                    " "
                )
            )
        }
    }
}

tasks.register("runFlatpak") {
    group = "compose desktop"
    dependsOn("packageFlatpak")
    doLast {
        exec {
            commandLine("flatpak run $appId".split(" "))
        }
    }
}