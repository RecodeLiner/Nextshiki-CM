package com.rcl.nextshiki.base.profile.settings

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.rcl.moko.MR.strings.settings_copy_token
import com.rcl.nextshiki.base.profile.ProfileComponent
import com.rcl.nextshiki.base.profile.settings.subcomponents.ButtonComponent
import com.rcl.nextshiki.base.profile.settings.subcomponents.FullLangRowComponent
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.StringDesc.LocaleType.System

class SettingsComponent(
    val navigator: StackNavigation<ProfileComponent.ProfileConfiguration>
) : ISettings {
    override val tokenButton = ButtonComponent(settings_copy_token)
    override val langRowComponent = FullLangRowComponent()
    fun returnToProfile(){
        navigator.bringToFront(ProfileComponent.ProfileConfiguration.MainProfileScreen)
    }

    override fun setupLanguage(code: String?) {
        if (code!=null) {
            StringDesc.localeType = StringDesc.LocaleType.Custom(code)
        }
        else {
            StringDesc.localeType = System
        }
    }

    override fun clearLanguage() {
        StringDesc.localeType = System
    }
}