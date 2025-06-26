package com.rcl.nextshiki.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.presentation.theme.NextShikiTheme
import com.rcl.nextshiki.presentation.theme.ThemeMode

@Composable
internal fun App(rootComponent: RootComponent) {
    val currentTheme by rootComponent.globalModule.themeColor.currentThemeMode.collectAsState(
        initial = ThemeMode.SYSTEM
    )

    NextShikiTheme(
        themeMode = currentTheme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            RootScreen(rootComponent)
        }
    }
}