package com.rcl.nextshiki.base.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.base.main.mainpage.MainNewsComponentScreen
import com.rcl.nextshiki.base.main.newspage.NewsPageScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainComponentScreen(component: MainComponent) {
    val stack by component.childStack.subscribeAsState()

    Children(
        stack = stack,
        animation = predictiveBackAnimation(
            backHandler = component::backHandler.get(),
            onBack = {
                component.onBack()
            },
        ),
    ) { newsLevelChild ->
        when (val instance = newsLevelChild.instance) {
            is MainComponent.NewsLevelChild.MainNewsScreen -> MainNewsComponentScreen(instance.component)
            is MainComponent.NewsLevelChild.NewsPageScreen -> NewsPageScreen(instance.component)
        }
    }
}