package com.rcl.nextshiki.base.profile.settings.subcomponents

import com.rcl.moko.MR.strings.settings_en_lang
import com.rcl.moko.MR.strings.settings_reset_lang
import com.rcl.moko.MR.strings.settings_ru_lang

class FullLangRowComponent : IFullLangRow {
    override val ruButton: LangRowComponent = LangRowComponent(settings_ru_lang, "ru")
    override val engButton: LangRowComponent = LangRowComponent(settings_en_lang, "en")
    override val resetButton: LangRowComponent = LangRowComponent(settings_reset_lang, null)

    val list = listOf(ruButton, engButton,resetButton)
}