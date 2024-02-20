package com.rcl.nextshiki.elements

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.rcl.nextshiki.AndroidApp
import com.rcl.nextshiki.AndroidApp.Companion.clipboardManager
import org.koin.core.context.GlobalContext


internal actual fun copyToClipboard(text: String) {
    val clip = ClipData.newPlainText("label", text)
    clipboardManager.setPrimaryClip(clip)
}