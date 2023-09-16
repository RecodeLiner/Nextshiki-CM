package com.rcl.nextshiki.screens

import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.resources.StringResource

data class Routes(
    val name: StringResource,
    val screen: Screen,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
)