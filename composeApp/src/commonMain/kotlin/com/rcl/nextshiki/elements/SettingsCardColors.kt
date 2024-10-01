package com.rcl.nextshiki.elements

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable

@Composable
fun getNotSelectedCardColor(color: ColorScheme): CardColors {
    return CardDefaults.cardColors(
        containerColor = color.surfaceVariant,
        contentColor = contentColorFor(color.surfaceVariant)
    )
}

@Composable
fun getSelectedCardColor(color: ColorScheme): CardColors {
    return CardDefaults.cardColors(
        contentColor = color.surfaceVariant,
        containerColor = contentColorFor(color.surfaceVariant)
    )
}
