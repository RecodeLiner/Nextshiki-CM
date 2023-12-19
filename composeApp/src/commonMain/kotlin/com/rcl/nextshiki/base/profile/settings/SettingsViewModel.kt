package com.rcl.nextshiki.base.profile.settings

import com.rcl.nextshiki.copyToClipboard
import com.rcl.nextshiki.elements.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class SettingsViewModel : ViewModel() {
    init {
        initialize(CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate))
    }

    fun copy(text: String){
        copyToClipboard(text)
    }
}