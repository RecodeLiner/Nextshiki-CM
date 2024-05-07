package com.rcl.nextshiki.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveRow(firstRow: LazyListScope.() -> Unit, secondRow: LazyListScope.() -> Unit) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when(widthSizeClass) {
        Compact -> {
            LazyColumn {
                firstRow()
                secondRow()
            }
        }
        else -> {
            Row {
                LazyColumn(content = firstRow, modifier = Modifier.weight(1f))
                LazyColumn(content = secondRow, modifier = Modifier.weight(1f))
            }
        }
    }
}