package com.rcl.nextshiki.base.search

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.rcl.nextshiki.base.search.mainsearchscreen.MainSearchComponentScreen
import com.rcl.nextshiki.base.search.searchedelementscreen.SearchedElementComponentScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun SearchComponentScreen(component: SearchComponent) {
    Children(
        stack = component.childStack,
        animation = predictiveBackAnimation(
            backHandler = component::backHandler.get(),
            onBack = {
                component.onBack()
            },
        ),
    ) { searchLevelChild ->
        when (val instance = searchLevelChild.instance) {
            is SearchComponent.SearchLevelChild.MainSearchScreen -> MainSearchComponentScreen(instance.component)
            is SearchComponent.SearchLevelChild.SearchedElementScreen -> SearchedElementComponentScreen(instance.component)
        }
    }
}