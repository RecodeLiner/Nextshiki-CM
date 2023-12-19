package com.rcl.nextshiki.base.profile.settings.subcomponents

interface IButton {
    val text: String
    fun onClicked(text: String);
}