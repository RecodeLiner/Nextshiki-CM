package com.rcl.nextshiki.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rcl.nextshiki.theme.ThemeColors.darkColor
import com.rcl.nextshiki.theme.ThemeColors.lightColor

object Theme {

    val AppShapes = Shapes(
        extraSmall = RoundedCornerShape(16.dp),
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(24.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(32.dp)
    )

    /*@Composable
    fun getGoogleSansRegularFont(): FontFamily {
        return fontFamilyResource(opensansregular)
    }*/

    @Composable
    fun getTypography() = Typography(
        bodyLarge = TextStyle(
            //fontFamily = getGoogleSansRegularFont(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            //fontFamily = getGoogleSansRegularFont(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        displayLarge = TextStyle(
            //fontFamily = getGoogleSansRegularFont(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.5.sp
        )
    )

    @Composable
    internal fun AppTheme(
        useDarkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (useDarkTheme) {
            darkColor
        } else {
            lightColor
        }

        MaterialTheme(
            colorScheme = colors,
            typography = getTypography(),
            shapes = AppShapes,
            content = {
                Surface(content = content)
            }
        )
    }
}
