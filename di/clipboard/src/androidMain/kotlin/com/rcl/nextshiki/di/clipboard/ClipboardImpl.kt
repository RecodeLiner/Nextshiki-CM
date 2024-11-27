package com.rcl.nextshiki.di.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import kotlin.isInitialized

actual class ClipboardImpl : IClipboard {
    companion object {
        lateinit var clipboardManager: ClipboardManager
        private fun isInitialized() = ::clipboardManager.isInitialized
    }

    actual override fun copyToClipboard(str: String) {
        if (isInitialized()) {
            val clip = ClipData.newPlainText("label", str)
            clipboardManager.setPrimaryClip(clip)
        }
    }
}