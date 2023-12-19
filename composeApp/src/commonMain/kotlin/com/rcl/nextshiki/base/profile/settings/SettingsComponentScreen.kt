package com.rcl.nextshiki.base.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.elements.getNotSelectedCardColor
import com.rcl.nextshiki.elements.getSelectedCardColor
import com.rcl.nextshiki.noRippleClickable
import com.rcl.nextshiki.setupLanguage
import com.rcl.nextshiki.strings.MainResStrings
import com.rcl.nextshiki.strings.MainResStrings.settings
import com.rcl.nextshiki.strings.MainResStrings.settings_lang_title
import io.github.skeptick.libres.LibresSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComponentScreen(settingsComponent: SettingsComponent) {
    val colorScheme = MaterialTheme.colorScheme

    val vm by derivedStateOf { SettingsComposableViewModel() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = settings) },
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { vm.copyTheme(colorScheme) }) {
                            Text(text = MainResStrings.settings_copy_theme)
                        }
                        Button(onClick = { vm.copy(KtorModel.token.value) }) {
                            Text(text = settingsComponent.tokenButton.text)
                        }
                    }
                }

                item {
                    Text(
                        text = settings_lang_title
                    )
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
                        settingsComponent.langRowComponent.list.forEach { component ->
                            val selected = MutableValue(LibresSettings.languageCode == component.lang_code)
                            Card(
                                colors = if (selected.value) getSelectedCardColor(colorScheme) else getNotSelectedCardColor(colorScheme),
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        selected.update { true }
                                        setupLanguage(component.lang_code)
                                    }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxSize().padding(15.dp),
                                    text = component.lang,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}