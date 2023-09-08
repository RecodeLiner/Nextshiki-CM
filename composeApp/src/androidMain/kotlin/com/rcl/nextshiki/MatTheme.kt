package com.rcl.nextshiki

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rcl.nextshiki.theme.ThemeColors.darkColor
import com.rcl.nextshiki.theme.ThemeColors.lightColor

object MatTheme {
    private val AppTypography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )

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
        val shapes = Shapes(
            small = RoundedCornerShape(16.dp),
            medium = RoundedCornerShape(24.dp), //card shape
            large = RoundedCornerShape(0.dp),
            extraSmall = RoundedCornerShape(16.dp), //dropdown shape
        )

        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content,
            shapes = shapes
        )
    }
}