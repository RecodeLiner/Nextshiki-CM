package com.rcl.nextshiki.elements

import com.rcl.nextshiki.models.currentuser.TokenModel
import com.seiko.imageloader.ImageLoader

internal expect fun openUrl(url: String?)
internal expect fun generateImageLoader(): ImageLoader

internal expect suspend fun getToken(isFirst: Boolean, code: String): TokenModel
internal expect fun copyToClipboard(text: String)