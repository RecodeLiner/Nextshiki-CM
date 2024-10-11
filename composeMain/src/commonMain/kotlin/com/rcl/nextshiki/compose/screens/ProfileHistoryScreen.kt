package com.rcl.nextshiki.compose.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.rcl.nextshiki.SharedRes.strings.history_page
import com.rcl.nextshiki.components.profilecomponent.historypage.ProfileHistoryComponent
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHistoryScreen(profileHistoryComponent: ProfileHistoryComponent) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0),
                navigationIcon = {
                    IconButton(
                        onClick = profileHistoryComponent::navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon"
                        )
                    }
                },
                title = {
                    Text(text = stringResource(history_page))
                }
            )
        }
    ) {

    }
}