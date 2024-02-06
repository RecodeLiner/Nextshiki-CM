package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import OperatingSystem
import OperatingSystem.*
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import okio.Path.Companion.toOkioPath
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.net.URI

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}


internal actual fun copyToClipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}