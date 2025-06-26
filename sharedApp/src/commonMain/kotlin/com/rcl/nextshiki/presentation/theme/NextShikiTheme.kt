package com.rcl.nextshiki.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.rcl.nextshiki.utils.expected.currentPlatform
import com.rcl.nextshiki.utils.expected.getDynamicColor

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun NextShikiTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {

    MaterialExpressiveTheme(
        colorScheme = when (themeMode) {
            ThemeMode.DARK -> darkColorScheme()
            ThemeMode.LIGHT -> lightColorScheme()
            ThemeMode.SYSTEM -> if (currentPlatform == "Android") {
                getDynamicColor(isSystemInDarkTheme())
            } else {
                if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            }
        },
        content = { Surface(content = content) }
    )
}