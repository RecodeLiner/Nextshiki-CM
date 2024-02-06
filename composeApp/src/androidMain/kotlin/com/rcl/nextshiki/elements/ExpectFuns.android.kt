package com.rcl.nextshiki.elements

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import com.rcl.nextshiki.AndroidApp.Companion.INSTANCE
import com.rcl.nextshiki.AndroidApp.Companion.clipboardManager

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    INSTANCE.startActivity(intent)
}

internal actual fun copyToClipboard(text: String) {
    val clip = ClipData.newPlainText("label", text)
    clipboardManager.setPrimaryClip(clip)
}