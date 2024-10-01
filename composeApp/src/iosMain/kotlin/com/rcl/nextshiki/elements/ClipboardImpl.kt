package com.rcl.nextshiki.elements

import UIKit
import com.rcl.nextshiki.di.clipboard.IClipboard

class ClipboardImpl: IClipboard {
    override fun copyToClipboard(str: String) {
        UIPasteboard.general.string = str
    }
}
