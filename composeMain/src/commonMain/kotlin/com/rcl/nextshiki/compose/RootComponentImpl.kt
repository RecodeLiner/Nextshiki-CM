package com.rcl.nextshiki.compose

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

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun App(
    rootComponent: RootComponent,
    seedColor: Color = MaterialTheme.colorScheme.primary
) {
    DecomposeExperimentFlags.duplicateConfigurationsEnabled = true
    AppTheme(
        seedColor = seedColor
    ) {
        RootComponentImpl(rootComponent)
    }
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