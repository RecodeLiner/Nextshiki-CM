package com.rcl.nextshiki.elements

import android.content.ClipData
import com.rcl.nextshiki.AndroidApp.Companion.clipboardManager
import com.rcl.nextshiki.elements.Platforms.Mobile


internal actual fun copyToClipboard(text: String) {
    val clip = ClipData.newPlainText("label", text)
    clipboardManager.setPrimaryClip(clip)
}

internal actual fun currentPlatform(): Platforms {
    return Mobile
}