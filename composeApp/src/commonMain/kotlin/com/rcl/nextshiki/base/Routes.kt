package com.rcl.nextshiki.base

import androidx.compose.ui.graphics.vector.ImageVector
import dev.icerock.moko.resources.StringResource

data class Routes(
    val name: StringResource,
    val configuration: RootComponent.TopLevelConfiguration,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
)
