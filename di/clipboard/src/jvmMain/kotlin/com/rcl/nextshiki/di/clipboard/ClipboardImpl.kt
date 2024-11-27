package com.rcl.nextshiki.di.clipboard

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

actual class ClipboardImpl : IClipboard {
    private val clipboardManager: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    actual override fun copyToClipboard(str: String) {
        val selection = StringSelection(str)
        clipboardManager.setContents(selection, selection)
    }
}