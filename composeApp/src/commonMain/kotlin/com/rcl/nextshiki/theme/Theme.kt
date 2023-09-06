package com.rcl.nextshiki.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Theme {
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
        primary = Color(0xFF715859),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFCDBDB),
        onPrimaryContainer = Color(0xFF281717),
        inversePrimary = Color(0xFFDFBFC0),
        secondary = Color(0xFF6B5B5B),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFF4DDDE),
        onSecondaryContainer = Color(0xFF241919),
        tertiary = Color(0xFF775658),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFD9DB),
        onTertiaryContainer = Color(0xFF2C1516),
        error = Color(0xFFB3261E),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFF9DEDC),
        onErrorContainer = Color(0xFF410E0B),
        background = Color(0xFFFFFBFA),
        onBackground = Color(0xFF1E1B1B),
        surface = Color(0xFFFFFBFA),
        onSurface = Color(0xFF1E1B1B),
        inverseSurface = Color(0xFF332F2F),
        inverseOnSurface = Color(0xFFF7EFEE),
        surfaceVariant = Color(0xFFE9E0E0),
        onSurfaceVariant = Color(0xFF4A4645),
        outline = Color(0xFF7A7574)
    )

    private val AppShapes = Shapes(
        extraSmall = RoundedCornerShape(2.dp),
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(32.dp)
    )

    private val AppTypography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )
    /*private val googleSans = FontFamily(
        Font(),
        Font(R.font.opensansbold, FontWeight.Bold),
    )

    val Typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = googleSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ) ,
        titleLarge = TextStyle(
            fontFamily = googleSans,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ) ,
        displayLarge = TextStyle(
            fontFamily = googleSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.5.sp
        ) ,
        /*
        labelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
        */
    )*/

    @Composable
    internal fun AppTheme(
        useDarkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable() () -> Unit
    ) {
        val colors = if (useDarkTheme) {
            darkColor
        } else {
            lightColor
        }

        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            shapes = AppShapes,
            content = {
                Surface(content = content)
            }
        )
    }
}
