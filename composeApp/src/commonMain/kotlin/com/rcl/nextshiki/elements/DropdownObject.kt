package com.rcl.nextshiki.elements

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

@Composable
internal expect fun GetDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
)

@Composable
internal expect fun GetDropdownMenuItem(
    onClick: () -> Unit,
    text: @Composable () -> Unit,
)