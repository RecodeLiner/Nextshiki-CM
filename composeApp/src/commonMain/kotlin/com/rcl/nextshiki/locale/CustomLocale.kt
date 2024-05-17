package com.rcl.nextshiki.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.StringDesc.LocaleType

object CustomLocale {
    @Composable
    fun StringResource.getLocalizableString(vararg args: Any) : String {
        Locale.current.language
        currentMRLocale.value
        return stringResource(resource = this, args = args)
    }

    @Composable
    fun getLangRes(english: String?, russian: String?): String? {
        return if (currentMRLocale.value == LocaleType.System) {
            if (Locale.current.language == "ru") russian
            else english
        } else {
            if (currentMRLocale.value == LocaleType.Custom("ru")) russian
            else english
        }
    }

    val currentMRLocale = mutableStateOf<LocaleType>(LocaleType.System)
}