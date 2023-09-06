package com.rcl.nextshiki.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

actual object ProfileScreen: Screen {
    private fun readResolve(): Any = ProfileScreen

    @Composable
    override fun Content() {

    }
}