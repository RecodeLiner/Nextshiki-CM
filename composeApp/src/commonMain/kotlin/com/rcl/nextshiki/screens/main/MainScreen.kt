package com.rcl.nextshiki.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalLayoutApi::class
    )
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
            val isCompat = calculateWindowSizeClass().widthSizeClass == Compact
            Box (modifier = Modifier.padding(paddings)) {
                FlowColumn {
                    BoxWithConstraints (
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        AnimatedContent(!vm.calendarList.isEmpty()) {  empty ->
                            if (empty) {
                                vm.nearTitle.value.let { nearTitle ->
                                    vm.previewName.value.let { name ->
                                        Box(modifier = Modifier.width(if(isCompat) maxWidth else maxWidth/2 )){
                                            CalendarCard(
                                                name = name,
                                                link = nearTitle?.anime?.image?.preview!!,
                                                time = nearTitle.nextEpisodeAt!!
                                            )
                                        }
                                    }
                                }
                            } else {
                                Box(
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