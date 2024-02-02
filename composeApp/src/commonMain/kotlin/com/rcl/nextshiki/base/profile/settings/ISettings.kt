package com.rcl.nextshiki.base.profile.settings

import com.rcl.nextshiki.base.profile.settings.subcomponents.ButtonComponent
import com.rcl.nextshiki.base.profile.settings.subcomponents.FullLangRowComponent

interface ISettings {
    val tokenButton : ButtonComponent
    val langRowComponent : FullLangRowComponent
    fun setupLanguage(code: String?)
    fun clearLanguage()
}