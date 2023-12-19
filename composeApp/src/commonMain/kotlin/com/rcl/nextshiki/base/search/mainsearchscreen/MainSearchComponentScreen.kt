package com.rcl.nextshiki.base.search.mainsearchscreen

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun MainSearchComponentScreen(component: MainSearchComponent) {
    val text by component.text.subscribeAsState()

    TextField(
        value = text,
        onValueChange = { value ->
            component.onTextChanged(value)
        }
    )
}