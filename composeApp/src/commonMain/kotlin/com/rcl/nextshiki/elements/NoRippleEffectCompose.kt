package com.rcl.nextshiki.elements

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.noRippleClickable(
    onClick: () -> Unit
) =
    this.then(
        Modifier.clickable(
            interactionSource = null,
            indication = null,
            onClick = onClick
        )
    )

