package com.rcl.nextshiki.di.clipboard

import platform.UIKit.UIPasteboard

actual class ClipboardImpl : IClipboard {
    actual override fun copyToClipboard(str: String) {
        UIPasteboard.general.string = str
    }
}