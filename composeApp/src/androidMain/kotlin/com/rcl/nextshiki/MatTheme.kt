package com.rcl.nextshiki

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rcl.nextshiki.theme.Theme.AppShapes
import com.rcl.nextshiki.theme.Theme.getTypography
import com.rcl.nextshiki.theme.ThemeColors.darkColor
import com.rcl.nextshiki.theme.ThemeColors.lightColor

object MatTheme {
    @Composable
    fun AppTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = true,
        content: @Composable () -> Unit
    ) {
        val colorScheme = when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            else -> if (darkTheme) darkColor else lightColor
        }

        val systemUiController = rememberSystemUiController()
        val view = LocalView.current

        if (!view.isInEditMode) {
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !darkTheme
                )
            }
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = getTypography(),
            content = content,
            shapes = AppShapes
        )
    }
}