package com.rcl.nextshiki.elements

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp

fun VerticalRoundedCornerShape(
    top: Dp,
    bottom: Dp
) = RoundedCornerShape(
    topStart = top,
    topEnd = top,
    bottomStart = bottom,
    bottomEnd = bottom
)

fun HorizontalRoundedCornerShape(
    start: Dp,
    end: Dp
) = RoundedCornerShape(
    topStart = start,
    bottomStart = start,
    topEnd = end,
    bottomEnd = end
)
