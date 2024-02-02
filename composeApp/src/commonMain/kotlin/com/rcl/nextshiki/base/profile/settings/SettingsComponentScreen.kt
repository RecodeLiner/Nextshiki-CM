package com.rcl.nextshiki.base.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rcl.moko.MR.strings.settings
import com.rcl.moko.MR.strings.settings_copy_theme
import com.rcl.moko.MR.strings.settings_lang_title
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.elements.getNotSelectedCardColor
import com.rcl.nextshiki.elements.getSelectedCardColor
import com.rcl.nextshiki.elements.noRippleClickable
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.StringDesc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComponentScreen(component: SettingsComponent) {
    val colorScheme = MaterialTheme.colorScheme

    val vm by derivedStateOf { SettingsComposableViewModel() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(settings)) },
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { vm.copyTheme(colorScheme) }) {
                            Text(text = stringResource(settings_copy_theme))
                        }
                        Button(onClick = { vm.copy(KtorModel.token.value) }) {
                            Text(text = stringResource(component.tokenButton.text))
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(settings_lang_title)
                    )
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp)) {
                        component.langRowComponent.list.forEach { lang ->
                            val selected = StringDesc.localeType.toString() == lang.lang_code
                            Card(
                                colors = if (selected) getSelectedCardColor(colorScheme) else getNotSelectedCardColor(
                                    colorScheme
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        component.setupLanguage(lang.lang_code)
                                    }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxSize().padding(15.dp),
                                    text = stringResource(lang.lang),
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