
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
    androidTarget()

    jvm()

    for (target in listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )) {
        target.binaries.framework {
            baseName = "SharedApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.decompose)
                implementation(libs.decompose.compose)
                implementation(libs.decompose.experiments)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datatime)
                implementation(libs.coil)
                implementation(compose.components.resources)
                implementation(libs.coil.network.ktor)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.bundles.datastore)
                implementation(libs.bundles.mvikotlin)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        androidMain {
            dependencies {
                implementation(compose.uiTooling)
                implementation(libs.androidx.activityCompose)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.ktor.client.engine.okhttp)
            }
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ktor.client.engine.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.engine.darwin)
            }
        }
    }
}

buildkonfig {
    fun getConfigProperties(): Properties {
        val propertiesFile = File("nextshikiAuth.properties")
        val properties = Properties()

        if (propertiesFile.exists()) {
            FileInputStream(propertiesFile).use { inputStream ->
                properties.load(inputStream)
            }
        }

        return properties
    }
    fun getProperty(propertyName: String, properties: Properties): String? {
        return properties.getProperty(propertyName) ?: System.getenv(propertyName)
    }
    val buildProperties = getConfigProperties()

    packageName = "com.rcl.nextshiki"
    defaultConfigs {
        buildConfigField(STRING, "appName", getProperty("appName", buildProperties), nullable = true)
        buildConfigField(STRING, "domain", getProperty("domain", buildProperties), nullable = true)
        buildConfigField(STRING, "clientId", null, nullable = true)
        buildConfigField(STRING, "clientSecret", null, nullable = true)
        buildConfigField(STRING, "userAgent", null, nullable = true)
        buildConfigField(STRING, "redirectURI", null, nullable = true)
        buildConfigField(STRING, "scope", null, nullable = true)
    }
    targetConfigs {
        for (target in listOf("android", "ios")){
            create(target) {
                buildConfigField(STRING, "clientId", getProperty("clientId", buildProperties), nullable = true)
                buildConfigField(STRING, "clientSecret", getProperty("clientSecret", buildProperties), nullable = true)
                buildConfigField(STRING, "userAgent", getProperty("userAgent", buildProperties), nullable = true)
                buildConfigField(STRING, "redirectURI", getProperty("redirectURI", buildProperties), nullable = true)
                buildConfigField(STRING, "scope", getProperty("scope", buildProperties), nullable = true)
            }
        }
        create("jvm") {
            buildConfigField(STRING, "clientId", getProperty("clientIDDesk", buildProperties), nullable = true)
            buildConfigField(STRING, "clientSecret", getProperty("clientSecretDesk", buildProperties), nullable = true)
            buildConfigField(STRING, "userAgent", getProperty("useruserAgentDeskAgent", buildProperties), nullable = true)
            buildConfigField(STRING, "redirectURI", getProperty("redirectURIDesk", buildProperties), nullable = true)
            buildConfigField(STRING, "scope", getProperty("scopeDesk", buildProperties), nullable = true)
        }
    }
}

android {
    namespace = "com.rcl.nextshiki"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        targetSdk = 36

        applicationId = "com.rcl.nextshiki.androidApp"
        versionCode = project.properties["version.code"].toString().toInt()
        versionName = project.properties["version.name"].toString()
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )
            packageName = "Nextshiki"
            packageVersion = project.properties["version.name"].toString()

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxIcon.png"))
                modules("jdk.security.auth")
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
                bundleID = "com.rcl.nextshiki.desktopApp"
            }
        }
    }
}

dependencies {
    for (dependency in listOf(
        libs.room.compiler
    )) {
        with(dependency) {
            add("kspAndroid", this)
            add("kspJvm", this)
            add("kspIosX64", this)
            add("kspIosArm64", this)
            add("kspIosSimulatorArm64", this)
        }
    }
}