package com.rcl.nextshiki.utils

enum class Platform {
    Mobile,
    Desktop
}

expect fun getCurrentPlatform() : Platform