package com.rcl.nextshiki.base.search.searchedelementscreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun SearchedElementComponentScreen(searchedElementComponent: SearchedElementComponent) {
    val searched by searchedElementComponent.searchedElement.subscribeAsState()

    if (searched.name != null){
        Text(searched.name!!)
    }
}