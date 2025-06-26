package com.rcl.nextshiki.utils.expected

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun getDynamicColor(isDark: Boolean) : ColorScheme?