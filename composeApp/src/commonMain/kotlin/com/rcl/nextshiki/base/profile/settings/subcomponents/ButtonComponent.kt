package com.rcl.nextshiki.base.profile.settings.subcomponents

import com.rcl.nextshiki.elements.copyToClipboard
import dev.icerock.moko.resources.StringResource

class ButtonComponent(override val text: StringResource) : IButton {
    override fun onClicked(text: String) {
        copyToClipboard(text)
    }
}