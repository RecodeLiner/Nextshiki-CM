import org.gradle.kotlin.dsl.multiplatformResources
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.moko)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.moko.base)
        }
    }
}

multiplatformResources {
    resourcesPackage.set("com.rcl.nextshiki")
    resourcesClassName.set("SharedRes")
}

fun getConfigProperties(): Properties {
    val propertiesFile = File("../nextshikiAuth.properties")
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

val redirectURI = getProperty("redirectURI", buildProperties)
val clientId = getProperty("clientId", buildProperties)
val clientSecret = getProperty("clientSecret", buildProperties)
val domain = getProperty("domain", buildProperties)
val userAgent = getProperty("userAgent", buildProperties)
val clientIDDesk = getProperty("clientIDDesk", buildProperties)
val clientSecretDesk = getProperty("clientSecretDesk", buildProperties)
val redirectURIDesk = getProperty("redirectURIDesk", buildProperties)
val scope = getProperty("scope", buildProperties)
val scopeDesk = getProperty("scopeDesk", buildProperties)
val userAgentDesk = getProperty("userAgentDesk", buildProperties)

buildConfig {
    useKotlinOutput { internalVisibility = false }
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

android {
    namespace = "com.rcl.nextshiki"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }
}