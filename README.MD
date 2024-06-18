# Nextshiki

Nextshiki - unofficial MD3 client for [Shikimori](https://shikimori.one), written
on [Kotlin](https://kotlinlang.org),
[Jetbrains Compose](https://jetbrains.com/lp/compose-multiplatform/), [Ktor](https://ktor.io/)
and more.
Client based on official API [Shikimori](https://shikimori.one/api/doc).

![Static Badge](https://img.shields.io/badge/RecodeLiner-Nextshiki_CM-blue)
![Github top language](https://img.shields.io/github/languages/top/RecodeLiner/Nextshiki-CM)

## Before running!

- check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
- install JDK 8 on your machine
- add `local.properties` file to the project root and set a path to Android SDK there
- run `./gradlew podInstall` in the project root

## Setup authorization

To build application, you need to put in your `nextshikiAuth.properties` file auth data.  
Get auth data you can from [official site](https://shikimori.me/oauth/applications).  
After this, you can set up your file like this:

```properties
# for mobile app:
clientId=""
clientSecret=""
domain="https://shikimori.one"
redirectURI=""
userAgent="Nextshiki"
scope=""
# for desktop app:
userAgentDesk="Nextshiki-desk"
clientIDDesk=""
clientSecretDesk=""
redirectURIDesk="urn:ietf:wg:oauth:2.0:oob"
scopeDesk=""
```

If you want to set up only mobile or only desktop-app-set fields empty.

### Android

To run the application on android device/emulator:

- open project in Android Studio and run imported android run configuration

To build the application bundle:

- run `./gradlew :composeApp:assembleDebug`
- find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

### Desktop

Run the desktop application: `./gradlew :composeApp:run`

For build release for your current OS run: `./gradlew packageReleaseDistributionForCurrentOS`
or for build a jar you can run: `./gradlew packageReleaseUberJarForCurrentOS`  
Then just find it in `composeApp\build\libs`

### iOS

To run the application on iPhone device/simulator:

- Open `iosApp/iosApp.xcworkspace` in Xcode and run standard configuration
- Or
  use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
  for Android Studio

### For auto build using GitHub actions:

To auto build your application using GitHub actions, you need to put these secrets:

```gitexclude
# For mobile:
CLIENT_ID
REDIRECT_URI
CLIENT_SECRET
CLIENT_DOMAIN
USER_AGENT
SCOPE
# For desktop:
CLIENT_ID_DESK
CLIENT_SECRET_DESK
REDIRECT_URI_DESK
SCOPE_DESK
USER_AGENT_DESK
```

Due to specific BuildConfig plugin, you need to put values with double quotes in value of GitHub
actions like:

```gitignore
name: USER_AGENT
secret: "Nextshiki"
```