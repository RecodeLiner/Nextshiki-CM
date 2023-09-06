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

object MatTheme {
    private val darkColor = darkColorScheme(
        primary = Color(0xFFA4C9FF),
        onPrimary = Color(0xFF003060),
        primaryContainer = Color(0xFF17487D),
        onPrimaryContainer = Color(0xFFD3E3FF),
        inversePrimary = Color(0xFF355F97),
        secondary = Color(0xFFBCC7DB),
        onSecondary = Color(0xFF263141),
        secondaryContainer = Color(0xFF3D4758),
        onSecondaryContainer = Color(0xFFD8E3F8),
        tertiary = Color(0xFFDABCE2),
        onTertiary = Color(0xFF3D2846),
        tertiaryContainer = Color(0xFF553F5D),
        onTertiaryContainer = Color(0xFFF7D9FF),
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
        errorContainer = Color(0xFF8C1D18),
        onErrorContainer = Color(0xFFF9DEDC),
        background = Color(0xFF1B1B1B),
        onBackground = Color(0xFFE2E2E2),
        surface = Color(0xFF1B1B1B),
        onSurface = Color(0xFFE2E2E2),
        inverseSurface = Color(0xFFE2E2E2),
        inverseOnSurface = Color(0xFF303030),
        surfaceVariant = Color(0xFF474747),
        onSurfaceVariant = Color(0xFFC6C6C6),
        outline = Color(0xFF919191)
    )
    private val lightColor = lightColorScheme(
        primary = Color(0xFFA4C9FF),
        onPrimary = Color(0xFF003060),
        primaryContainer = Color(0xFF17487D),
        onPrimaryContainer = Color(0xFFD3E3FF),
        inversePrimary = Color(0xFF355F97),
        secondary = Color(0xFFBCC7DB),
        onSecondary = Color(0xFF263141),
        secondaryContainer = Color(0xFF3D4758),
        onSecondaryContainer = Color(0xFFD8E3F8),
        tertiary = Color(0xFFDABCE2),
        onTertiary = Color(0xFF3D2846),
        tertiaryContainer = Color(0xFF553F5D),
        onTertiaryContainer = Color(0xFFF7D9FF),
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
        errorContainer = Color(0xFF8C1D18),
        onErrorContainer = Color(0xFFF9DEDC),
        background = Color(0xFF1B1B1B),
        onBackground = Color(0xFFE2E2E2),
        surface = Color(0xFF1B1B1B),
        onSurface = Color(0xFFE2E2E2),
        inverseSurface = Color(0xFFE2E2E2),
        inverseOnSurface = Color(0xFF303030),
        surfaceVariant = Color(0xFF474747),
        onSurfaceVariant = Color(0xFFC6C6C6),
        outline = Color(0xFF919191)
    )

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