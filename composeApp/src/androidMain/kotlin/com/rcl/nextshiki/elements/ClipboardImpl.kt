package com.rcl.nextshiki.elements

import android.content.ClipData
import android.content.ClipboardManager
import com.rcl.nextshiki.di.clipboard.IClipboard

class ClipboardImpl(private val clipboardManager: ClipboardManager?): IClipboard {
    override fun copyToClipboard(str: String) {
        val clip = ClipData.newPlainText("label", str)
        clipboardManager?.setPrimaryClip(clip)
    }
}
