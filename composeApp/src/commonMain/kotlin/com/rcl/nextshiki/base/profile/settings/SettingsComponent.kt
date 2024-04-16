package com.rcl.nextshiki.base.profile.settings

import com.rcl.moko.MR.strings.settings_copy_token
import com.rcl.nextshiki.base.profile.settings.subcomponents.ButtonComponent
import com.rcl.nextshiki.base.profile.settings.subcomponents.FullLangRowComponent
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.StringDesc.LocaleType.System

class SettingsComponent(
    val navigateToProfile: () -> Unit
) {
    val tokenButton = ButtonComponent(settings_copy_token)
    val langRowComponent = FullLangRowComponent()
    fun returnToProfile() {
        navigateToProfile()
    }

    fun setupLanguage(code: String?) {
        if (code != null) {
            StringDesc.localeType = StringDesc.LocaleType.Custom(code)
        } else {
            StringDesc.localeType = System
        }
    }
}