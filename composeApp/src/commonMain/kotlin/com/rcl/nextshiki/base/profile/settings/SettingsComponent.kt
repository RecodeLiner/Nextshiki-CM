package com.rcl.nextshiki.base.profile.settings

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.rcl.nextshiki.base.profile.ProfileComponent
import com.rcl.nextshiki.base.profile.settings.subcomponents.ButtonComponent
import com.rcl.nextshiki.base.profile.settings.subcomponents.FullLangRowComponent
import com.rcl.nextshiki.strings.MainResStrings.settings_copy_token

class SettingsComponent(
    val navigator: StackNavigation<ProfileComponent.ProfileConfiguration>
) : ISettings {
    override val tokenButton = ButtonComponent(settings_copy_token)
    override val langRowComponent = FullLangRowComponent()
    fun returnToProfile(){
        navigator.bringToFront(ProfileComponent.ProfileConfiguration.MainProfileScreen)
    }
}