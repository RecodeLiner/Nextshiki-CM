package com.rcl.nextshiki.elements

internal expect fun copyToClipboard(text: String)
internal expect fun currentPlatform() : Platforms

enum class Platforms {
    Mobile,
    Desktop
}