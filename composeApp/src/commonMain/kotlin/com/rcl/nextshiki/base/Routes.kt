package com.rcl.nextshiki.base

import androidx.compose.ui.graphics.vector.ImageVector

data class Routes(
    val name: String,
    val configuration: RootComponent.TopLevelConfiguration,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
)