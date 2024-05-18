package com.rcl.nextshiki.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.StringDesc.LocaleType

object CustomLocale {
    @Composable
    fun StringResource.getLocalizableString(vararg args: Any): String {
        Locale.current.language
        currentLocal.value.first
        return stringResource(resource = this, args = args)
    }

    @Composable
    fun getLangRes(english: String?, russian: String?): String? {
        return if (currentLocal.value.first == LocaleType.System) {
            if (Locale.current.language == "ru") russian
            else english
        } else {
            if (currentLocal.value.first == LocaleType.Custom("ru")) russian
            else english
        }
    }

    fun getCurrentLocale(): String {
        return if (currentLocal.value.second != null) {
            currentLocal.value.second!!
        } else if (Locale.current.language == "ru") {
            "ru"
        } else {
            "en"
        }
    }

    val currentLocal = mutableStateOf(Pair<LocaleType, String?>(LocaleType.System, null))
}