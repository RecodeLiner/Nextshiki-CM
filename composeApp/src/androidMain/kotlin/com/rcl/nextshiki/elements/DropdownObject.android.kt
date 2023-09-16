package com.rcl.nextshiki.elements

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable

@Composable
internal actual fun GetDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        content = content
    )
}

@Composable
internal actual fun GetDropdownMenuItem(
    onClick: () -> Unit,
    text: @Composable () -> Unit,
) {
    DropdownMenuItem(
        onClick = onClick,
        text = text
    )
}