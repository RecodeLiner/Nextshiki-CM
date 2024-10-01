package com.rcl.nextshiki.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveRow(firstRow: LazyListScope.() -> Unit, secondRow: LazyListScope.() -> Unit) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    when (widthSizeClass) {
        Compact -> {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                firstRow()
                secondRow()
            }
        }

        else -> {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    content = firstRow,
                    modifier = Modifier.weight(1f)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    content = secondRow,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
