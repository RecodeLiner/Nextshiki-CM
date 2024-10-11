package com.rcl.nextshiki.di.language

import java.util.Locale

actual fun getCurrentLanguage(): String {
    return Locale.getDefault().language
}