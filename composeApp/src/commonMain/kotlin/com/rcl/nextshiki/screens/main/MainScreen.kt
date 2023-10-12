package com.rcl.nextshiki.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.elements.CalendarCard
import com.rcl.nextshiki.getString

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    @Composable
    override fun Content() {
        val vm = rememberScreenModel { MainViewModel() }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = getString(MR.strings.bottom_main)
                        )
                    },
                    actions = {

                    }
                )
            }
        ) { paddings ->
            Box(modifier = Modifier.padding(paddings)) {
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(220.dp),
                    ) {
                        AnimatedContent(!vm.calendarList.isEmpty()) {  empty ->
                            if (empty) {
                                vm.nearTitle.value.let { nearTitle ->
                                    vm.previewName.value.let { name ->
                                        CalendarCard(
                                            name = name,
                                            link = nearTitle?.anime?.image?.preview!!,
                                            time = nearTitle.nextEpisodeAt!!
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}