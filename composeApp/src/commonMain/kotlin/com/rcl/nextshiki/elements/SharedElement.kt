package com.rcl.nextshiki.elements

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun withLocalSharedTransition(content: @Composable SharedTransitionScope.() -> Unit) =
    with(LocalSharedTransitionScope.current) { content() }


@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("SharedTransitionScope not provided")
}

val LocalAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope> {
    error("AnimatedContentScope not provided")
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScopeProvider(content: @Composable SharedTransitionScope.() -> Unit) {
    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            content()
        }
    }
}

@Composable
fun AnimatedVisibilityScope.ProvideAnimatedVisibilityScope(
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalAnimatedVisibilityScope provides this,
    content
)