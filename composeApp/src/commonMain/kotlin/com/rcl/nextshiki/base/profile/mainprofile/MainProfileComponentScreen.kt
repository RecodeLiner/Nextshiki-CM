package com.rcl.nextshiki.base.profile.mainprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainProfileComponentScreen(component: MainProfileComponent) {
    val auth = component.isAuth
    Column {
        Text(
            if (auth) {
                "Authed"
            } else {
                "Not authed"
            }
        )
        Button(onClick = { component.navigateToSettings() }) {
            Text("Navigate To Settings")
        }
    }
}