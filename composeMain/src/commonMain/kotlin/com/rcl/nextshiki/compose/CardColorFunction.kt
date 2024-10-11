package com.rcl.nextshiki.compose

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
fun getCardColors(isSelected: Boolean): CardColors {
    val colorScheme = MaterialTheme.colorScheme
    return if (isSelected) {
        CardDefaults.cardColors(
            contentColor = colorScheme.surfaceVariant,
            containerColor = contentColorFor(colorScheme.surfaceVariant)
        )
    } else {
        CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant,
            contentColor = contentColorFor(colorScheme.surfaceVariant)
        )
    }
}

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