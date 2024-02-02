package com.rcl.nextshiki.base.profile.settings.subcomponents

import dev.icerock.moko.resources.StringResource

interface IButton {
    val text: StringResource
    fun onClicked(text: String);
}