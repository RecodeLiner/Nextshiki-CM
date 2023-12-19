package com.rcl.nextshiki.base.profile.settings.subcomponents

import com.rcl.nextshiki.strings.MainResStrings.settings_en_lang
import com.rcl.nextshiki.strings.MainResStrings.settings_reset_lang
import com.rcl.nextshiki.strings.MainResStrings.settings_ru_lang

class FullLangRowComponent : IFullLangRow {
    override val ruButton: LangRowComponent = LangRowComponent(settings_ru_lang, "ru")
    override val engButton: LangRowComponent = LangRowComponent(settings_en_lang, "eng")
    override val resetButton: LangRowComponent = LangRowComponent(settings_reset_lang, null)

    val list = listOf(ruButton, engButton,resetButton)
}