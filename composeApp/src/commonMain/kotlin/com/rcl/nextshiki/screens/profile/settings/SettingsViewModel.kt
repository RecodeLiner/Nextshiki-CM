package com.rcl.nextshiki.screens.profile.settings

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import cafe.adriel.voyager.core.model.ScreenModel

class SettingsViewModel : ScreenModel {
    init {

    }

    @OptIn(ExperimentalStdlibApi::class)
    fun copyColorScheme(colorScheme: ColorScheme, clipboard: ClipboardManager) {
        val data =
            ("primary = Color(0x${colorScheme.primary.toArgb().toHexString()}),\n") +
                    ("onPrimary = Color(0x${colorScheme.onPrimary.toArgb().toHexString()}),\n") +
                    ("primaryContainer = Color(0x${colorScheme.primaryContainer.toArgb().toHexString()}),\n") +
                    ("onPrimaryContainer = Color(0x${colorScheme.onPrimaryContainer.toArgb().toHexString()}),\n") +
                    ("inversePrimary = Color(0x${colorScheme.inversePrimary.toArgb().toHexString()}),\n") +
                    ("secondary = Color(0x${colorScheme.secondary.toArgb().toHexString()}),\n") +
                    ("onSecondary = Color(0x${colorScheme.onSecondary.toArgb().toHexString()}),\n") +
                    ("secondaryContainer = Color(0x${colorScheme.secondaryContainer.toArgb().toHexString()}),\n") +
                    ("onSecondaryContainer = Color(0x${colorScheme.onSecondaryContainer.toArgb().toHexString()}),\n") +
                    ("tertiary = Color(0x${colorScheme.tertiary.toArgb().toHexString()}),\n") +
                    ("onTertiary = Color(0x${colorScheme.onTertiary.toArgb().toHexString()}),\n") +
                    ("tertiaryContainer = Color(0x${colorScheme.tertiaryContainer.toArgb().toHexString()}),\n") +
                    ("onTertiaryContainer = Color(0x${colorScheme.onTertiaryContainer.toArgb().toHexString()}),\n") +
                    ("error = Color(0x${colorScheme.error.toArgb().toHexString()}),\n") +
                    ("onError = Color(0x${colorScheme.onError.toArgb().toHexString()}),\n") +
                    ("errorContainer = Color(0x${colorScheme.errorContainer.toArgb().toHexString()}),\n") +
                    ("onErrorContainer = Color(0x${colorScheme.onErrorContainer.toArgb().toHexString()}),\n") +
                    ("background = Color(0x${colorScheme.background.toArgb().toHexString()}),\n") +
                    ("onBackground = Color(0x${colorScheme.onBackground.toArgb().toHexString()}),\n") +
                    ("surface = Color(0x${colorScheme.surface.toArgb().toHexString()}),\n") +
                    ("onSurface = Color(0x${colorScheme.onSurface.toArgb().toHexString()}),\n") +
                    ("inverseSurface = Color(0x${colorScheme.inverseSurface.toArgb().toHexString()}),\n") +
                    ("inverseOnSurface = Color(0x${colorScheme.inverseOnSurface.toArgb().toHexString()}),\n") +
                    ("surfaceVariant = Color(0x${colorScheme.surfaceVariant.toArgb().toHexString()}),\n") +
                    ("onSurfaceVariant = Color(0x${colorScheme.onSurfaceVariant.toArgb().toHexString()}),\n") +
                    ("outline = Color(0x${colorScheme.outline.toArgb().toHexString()})")
        clipboard.setText(AnnotatedString(data))
    }
}