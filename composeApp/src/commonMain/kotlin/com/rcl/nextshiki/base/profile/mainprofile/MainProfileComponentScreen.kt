package com.rcl.nextshiki.base.profile.mainprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rcl.nextshiki.base.profile.mainprofile.profile.AuthProfileObject
import com.rcl.nextshiki.base.profile.mainprofile.profile.ProfileObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileComponentScreen(component: MainProfileComponent) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    component.mainAuthedObject.value.name?.let {
                        Text(
                            text = it
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (component.isAuth.value) {
                ProfileObject()
            } else {
                AuthProfileObject(component.ktorRepository, component::updateAuthState)
            }
        }
    }
}