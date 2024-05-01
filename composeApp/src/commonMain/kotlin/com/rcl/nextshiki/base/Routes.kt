package com.rcl.nextshiki.base

import androidx.compose.ui.graphics.vector.ImageVector
import com.rcl.nextshiki.locale.LocalizedString

data class Routes(
    val name: (LocalizedString) -> String,
    val configuration: RootComponent.TopLevelConfiguration,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
)