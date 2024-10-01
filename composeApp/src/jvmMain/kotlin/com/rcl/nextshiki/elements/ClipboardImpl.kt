package com.rcl.nextshiki.elements

import com.rcl.nextshiki.di.clipboard.IClipboard
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

class ClipboardImpl(private val clipboard: Clipboard): IClipboard {
    override fun copyToClipboard(str: String) {
        val selection = StringSelection(str)
        clipboard.setContents(selection, selection)
    }
}
