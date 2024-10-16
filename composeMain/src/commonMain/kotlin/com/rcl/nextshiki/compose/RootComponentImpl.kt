package com.rcl.nextshiki.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.DecomposeExperimentFlags
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.compose.Theme.AppTheme
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

//TODO: Try to rewrite this part later
@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun App(
    rootComponent: RootComponent,
    seedColor: Color = MaterialTheme.colorScheme.primary,
    topAppBar: @Composable () -> Unit = {},
) {
    DecomposeExperimentFlags.duplicateConfigurationsEnabled = true
    AppTheme(
        seedColor = seedColor
    ) {
        Column {
            topAppBar()
            RootComponentImpl(rootComponent)
        }
    }
}

fun setupNapier() {
    Napier.base(DebugAntilog())
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun RootComponentImpl(rootComponent: RootComponent) {
    val lanRepo = rootComponent.vm.languageRepo
    listenLang(lanRepo)
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass

    when (widthSizeClass) {
        Compact -> {
            NavBar(rootComponent)
        }

        Medium -> {
            MediumScreen(rootComponent)
        }

        Expanded -> {
            ExpandedScreen(rootComponent)
        }
    }
}