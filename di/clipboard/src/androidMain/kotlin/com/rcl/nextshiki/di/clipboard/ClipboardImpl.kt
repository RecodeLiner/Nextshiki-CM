package com.rcl.nextshiki.di.clipboard

import android.content.ClipData
import android.content.ClipboardManager

actual class ClipboardImpl : IClipboard {
    private lateinit var clipboardManager: ClipboardManager
    fun setClipboard(clipboard: ClipboardManager?) : ClipboardImpl {
        if (clipboard != null) {
            clipboardManager = clipboard
        }
        return this
    }
    actual override fun copyToClipboard(str: String) {
        if (this::clipboardManager.isInitialized) {
            val clip = ClipData.newPlainText("label", str)
            clipboardManager.setPrimaryClip(clip)
        }
    }
}