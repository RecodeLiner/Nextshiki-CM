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
        return stringResource(this)
        return stringResource(resource = this, args = args)
    }

    val currentMRLocale = mutableStateOf<LocaleType>(LocaleType.System)
}