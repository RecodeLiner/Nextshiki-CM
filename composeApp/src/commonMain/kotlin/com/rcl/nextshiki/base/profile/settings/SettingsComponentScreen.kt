package com.rcl.nextshiki.base.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.rcl.mr.MR.strings.settings
import com.rcl.mr.MR.strings.settings_copy_theme
import com.rcl.mr.MR.strings.settings_copy_token
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.elements.copyToClipboard
import com.rcl.nextshiki.elements.noRippleClickable
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComponentScreen(component: SettingsComponent) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0),
                title = { Text(text = settings.getLocalizableString()) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            component.returnToProfile()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                item("langRow") {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(
                            component.supportedLanguageButtons,
                            key = { it.code ?: "systemCode" }) { lang ->
                            Card(modifier = Modifier.noRippleClickable {
                                component.setupSettingsLanguage(lang.code)
                            }) {
                                Text(
                                    lang.langName.getLocalizableString(),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }
                item("copyThemeButton") {
                    Button(onClick = {
                        copyTheme(colorScheme)
                    }) {
                        Text(settings_copy_theme.getLocalizableString())
                    }
                }
                item("copyToken") {
                    Button(onClick = {
                        copyToClipboard(KtorModel.token.value)
                    }) {
                        Text(settings_copy_token.getLocalizableString())
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun copyTheme(colorScheme: ColorScheme) {
    val data =
        ("primary = Color(0x${colorScheme.primary.toArgb().toHexString()}),\n") +
                ("onPrimary = Color(0x${colorScheme.onPrimary.toArgb().toHexString()}),\n") +
                ("primaryContainer = Color(0x${
                    colorScheme.primaryContainer.toArgb().toHexString()
                }),\n") +
                ("onPrimaryContainer = Color(0x${
                    colorScheme.onPrimaryContainer.toArgb().toHexString()
                }),\n") +
                ("inversePrimary = Color(0x${
                    colorScheme.inversePrimary.toArgb().toHexString()
                }),\n") +
                ("secondary = Color(0x${colorScheme.secondary.toArgb().toHexString()}),\n") +
                ("onSecondary = Color(0x${colorScheme.onSecondary.toArgb().toHexString()}),\n") +
                ("secondaryContainer = Color(0x${
                    colorScheme.secondaryContainer.toArgb().toHexString()
                }),\n") +
                ("onSecondaryContainer = Color(0x${
                    colorScheme.onSecondaryContainer.toArgb().toHexString()
                }),\n") +
                ("tertiary = Color(0x${colorScheme.tertiary.toArgb().toHexString()}),\n") +
                ("onTertiary = Color(0x${colorScheme.onTertiary.toArgb().toHexString()}),\n") +
                ("tertiaryContainer = Color(0x${
                    colorScheme.tertiaryContainer.toArgb().toHexString()
                }),\n") +
                ("onTertiaryContainer = Color(0x${
                    colorScheme.onTertiaryContainer.toArgb().toHexString()
                }),\n") +
                ("error = Color(0x${colorScheme.error.toArgb().toHexString()}),\n") +
                ("onError = Color(0x${colorScheme.onError.toArgb().toHexString()}),\n") +
                ("errorContainer = Color(0x${
                    colorScheme.errorContainer.toArgb().toHexString()
                }),\n") +
                ("onErrorContainer = Color(0x${
                    colorScheme.onErrorContainer.toArgb().toHexString()
                }),\n") +
                ("background = Color(0x${colorScheme.background.toArgb().toHexString()}),\n") +
                ("onBackground = Color(0x${colorScheme.onBackground.toArgb().toHexString()}),\n") +
                ("surface = Color(0x${colorScheme.surface.toArgb().toHexString()}),\n") +
                ("onSurface = Color(0x${colorScheme.onSurface.toArgb().toHexString()}),\n") +
                ("inverseSurface = Color(0x${
                    colorScheme.inverseSurface.toArgb().toHexString()
                }),\n") +
                ("inverseOnSurface = Color(0x${
                    colorScheme.inverseOnSurface.toArgb().toHexString()
                }),\n") +
                ("surfaceVariant = Color(0x${
                    colorScheme.surfaceVariant.toArgb().toHexString()
                }),\n") +
                ("onSurfaceVariant = Color(0x${
                    colorScheme.onSurfaceVariant.toArgb().toHexString()
                }),\n") +
                ("outline = Color(0x${colorScheme.outline.toArgb().toHexString()})")
    copyToClipboard(data)
}