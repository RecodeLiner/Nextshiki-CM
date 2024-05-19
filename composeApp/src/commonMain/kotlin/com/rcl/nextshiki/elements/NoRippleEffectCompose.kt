package com.rcl.nextshiki.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.noRippleClickable(
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    noinline onClick: () -> Unit,
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = remember { interactionSource },
                indication = null,
                onClick = onClick
            )
        )
    }
)