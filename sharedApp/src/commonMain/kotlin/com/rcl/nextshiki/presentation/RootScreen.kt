package com.rcl.nextshiki.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.value.getValue
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.presentation.screen.HomeScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootScreen(rootComponent: RootComponent) {
    val stack by rootComponent.childStack

    ChildStack(
        stack = stack,
        animation = stackAnimation(
            animator = fade() + scale(),
            predictiveBackParams = {
                PredictiveBackParams(
                    backHandler = rootComponent.backHandler,
                    onBack = rootComponent::navigateBack,
                    animatable = ::androidPredictiveBackAnimatable,
                )
            }
        )
    ) {
        when (val child = it.instance) {
            is RootComponent.ComponentChild.HomeChild -> HomeScreen(child.components)
            else -> {
                Box {
                    Text("Unknown child")
                }
            }
        }
    }
}