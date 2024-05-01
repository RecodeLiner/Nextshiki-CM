package com.rcl.nextshiki.base.profile.historypage

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.rcl.nextshiki.locale.Locale.getComposeLocalizedText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHistoryScreen(component: ProfileHistoryComponent) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0),
                navigationIcon = {
                    IconButton(
                        onClick = component::navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon"
                        )
                    }
                },
                title = {
                    Text(text = getComposeLocalizedText().history_page)
                }
            )
        }
    ) {

    }
}