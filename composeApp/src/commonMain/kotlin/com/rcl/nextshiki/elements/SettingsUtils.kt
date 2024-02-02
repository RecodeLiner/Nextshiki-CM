package com.rcl.nextshiki.elements

import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.Settings

val settings: Settings = Settings()

val currLink = mutableStateOf<String?>(null)