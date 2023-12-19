package com.rcl.nextshiki.base.profile.settings.subcomponents

import com.rcl.nextshiki.copyToClipboard

class ButtonComponent(override val text: String) : IButton {
    override fun onClicked(text: String) {
        copyToClipboard(text)
    }
}