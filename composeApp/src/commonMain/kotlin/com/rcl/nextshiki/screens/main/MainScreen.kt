package com.rcl.nextshiki.screens.main

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin

object MainScreen : Screen {
    @Composable
    override fun Content() {
        var text by remember { mutableStateOf("") }
        val a = koin.get<KtorRepository>()
        LaunchedEffect(Unit) {
            text = a.getCalendar()[0].anime!!.name!!
        }

        Column {
            Text(
                BuildConfig.USER_AGENT
            )
            Text(
                text
            )
        }

    }
}