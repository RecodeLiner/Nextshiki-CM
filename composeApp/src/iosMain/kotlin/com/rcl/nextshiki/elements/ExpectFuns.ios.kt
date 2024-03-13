package com.rcl.nextshiki.elements

import com.rcl.nextshiki.elements.Platforms.Mobile

internal actual fun copyToClipboard(text: String) {

}

internal actual fun currentPlatform(): Platforms {
    return Mobile
}