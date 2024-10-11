package com.rcl.nextshiki.di.language

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getCurrentLanguage() : String {
    return NSLocale.currentLocale.languageCode
}