package com.rcl.nextshiki.base.profile.historypage

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
import com.rcl.mr.SharedRes.strings.history_page
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString

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
                    Text(text = history_page.getLocalizableString())
                }
            )
        }
    ) {

    }
}
