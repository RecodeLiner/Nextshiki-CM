package com.rcl.nextshiki.base.profile.settings

import com.rcl.nextshiki.base.profile.settings.subcomponents.ButtonComponent
import com.rcl.nextshiki.base.profile.settings.subcomponents.FullLangRowComponent
import com.rcl.nextshiki.strings.MainResStrings.settings_copy_token

class SettingsComponent : ISettings {
    override val tokenButton = ButtonComponent(settings_copy_token)
    override val langRowComponent = FullLangRowComponent()
}