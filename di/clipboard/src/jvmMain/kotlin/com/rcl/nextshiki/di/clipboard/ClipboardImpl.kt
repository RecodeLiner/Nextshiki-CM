package com.rcl.nextshiki.di.clipboard

import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

actual class ClipboardImpl : IClipboard {
    private lateinit var clipboardManager: Clipboard
    fun setClipboard(clipboard: Clipboard): ClipboardImpl {
        clipboardManager = clipboard
        return this
    }
    actual override fun copyToClipboard(str: String) {
        if (this::clipboardManager.isInitialized) {
            val selection = StringSelection(str)
            clipboardManager.setContents(selection, selection)
        }
    }
}