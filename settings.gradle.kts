rootProject.name = "Nextshiki"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://packages.jetbrains.team/maven/p/kpm/public/")
        maven("https://jitpack.io")
    }
}

include(":sharedApp")
include(":composeMain")
include(":components")
include(":di:repository")
include(":di:settings")
include(":di:clipboard")
include(":di:paging")
include(":di:language")
include(":utils")
include(":resources")
include(":models")