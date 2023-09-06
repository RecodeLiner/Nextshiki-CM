package com.rcl.nextshiki.screens

import Nextshiki.composeApp.BuildConfig
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

object MainScreen : Screen {
    @Composable
    override fun Content() {
        Text(
           BuildConfig.USER_AGENT
        )
    }
}