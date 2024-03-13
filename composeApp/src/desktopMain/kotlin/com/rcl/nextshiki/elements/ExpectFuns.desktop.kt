package com.rcl.nextshiki.elements

import com.rcl.nextshiki.elements.Platforms.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection


internal actual fun copyToClipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}

internal actual fun currentPlatform(): Platforms {
    return Desktop
}