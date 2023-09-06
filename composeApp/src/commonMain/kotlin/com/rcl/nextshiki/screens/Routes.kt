package com.rcl.nextshiki.screens

import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen

data class Routes (
    val name: String,
    val screen: Screen,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
)