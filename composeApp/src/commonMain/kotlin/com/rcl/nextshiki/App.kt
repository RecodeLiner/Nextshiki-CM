package com.rcl.nextshiki

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.expandedScreen
import com.rcl.nextshiki.base.mediumScreen
import com.rcl.nextshiki.base.navBar
import com.rcl.nextshiki.di.Koin.setupKoin
import com.rcl.nextshiki.theme.Theme.AppTheme
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@Composable
fun App(rootComponent: RootComponent, seedColor: Color = MaterialTheme.colorScheme.primary, topAppBar: @Composable () -> Unit = {}) {
    setupNapier()
    AppTheme(
        seedColor = seedColor
    ) {
        Column {
            topAppBar.invoke()
            setupUI(rootComponent)
        }
    }
}

internal fun setupNapier() {
    Napier.base(DebugAntilog())
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun setupUI(rootComponent: RootComponent) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass

    when (widthSizeClass) {
        Compact -> {
            navBar(rootComponent)
        }

        Medium -> {
            mediumScreen(rootComponent)
        }

        Expanded -> {
            expandedScreen(rootComponent)
        }
    }
}