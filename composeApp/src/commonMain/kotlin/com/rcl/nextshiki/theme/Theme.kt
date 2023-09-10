package com.rcl.nextshiki.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rcl.nextshiki.MR.fonts.opensansregular.opensansregular
import com.rcl.nextshiki.theme.ThemeColors.darkColor
import com.rcl.nextshiki.theme.ThemeColors.lightColor
import dev.icerock.moko.resources.compose.fontFamilyResource

object Theme {

    private val AppShapes = Shapes(
        extraSmall = RoundedCornerShape(2.dp),
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(32.dp)
    )

    //val googleSansBold = fontFamilyResource(opensansbold)
    @Composable
    fun getGoogleSansRegularFont(): FontFamily {
        return fontFamilyResource(opensansregular)
    }
    @Composable
    fun getTypography() = Typography(
        bodyLarge = TextStyle(
            fontFamily = getGoogleSansRegularFont(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ) ,
        titleLarge = TextStyle(
            fontFamily = getGoogleSansRegularFont(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ) ,
        displayLarge = TextStyle(
            fontFamily = getGoogleSansRegularFont(),
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
            typography = typography,
            shapes = AppShapes,
            content = {
                Surface(content = content)
            }
        )
    }
}
