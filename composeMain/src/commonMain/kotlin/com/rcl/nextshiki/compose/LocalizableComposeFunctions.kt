package com.rcl.nextshiki.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.di.language.LanguageRepo
import dev.icerock.moko.resources.desc.StringDesc

@Composable
fun listenLang(lang: LanguageRepo) {
    val repoLanguage by lang.localeVar.subscribeAsState()
    if (repoLanguage.isBlank()) {
        StringDesc.localeType = StringDesc.LocaleType.System
    } else {
        StringDesc.localeType = StringDesc.LocaleType.Custom(repoLanguage)
    }
}

fun getLangRes(currentCode: String, russian: String, english: String) : String {
    //This function only for work with moko
    return if (currentCode == "ru") russian else english
}