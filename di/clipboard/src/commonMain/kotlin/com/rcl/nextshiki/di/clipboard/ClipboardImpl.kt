package com.rcl.nextshiki.di.clipboard

expect class ClipboardImpl() : IClipboard {
    override fun copyToClipboard(str: String)
}