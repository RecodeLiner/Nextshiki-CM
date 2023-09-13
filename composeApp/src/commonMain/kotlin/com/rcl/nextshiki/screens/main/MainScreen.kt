package com.rcl.nextshiki.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.elements.CalendarCard
import com.rcl.nextshiki.getString

@OptIn(ExperimentalMaterial3Api::class)
object MainScreen : Screen {
    @ExperimentalAnimationApi
    @Composable
    override fun Content() {
        val vm = remember { MainViewModel() }

        LaunchedEffect(Unit){
            vm.init(this)
        }

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
        ) {
            Box(modifier = Modifier.padding(it)){
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(220.dp),
                    ) {
                        AnimatedContent(!vm.calendarList.isEmpty()){
                            if(it){
                                CalendarCard(vm.previewName ,vm.nearTitle.anime!!.image!!.preview!!, vm.nearTitle.nextEpisodeAt!!)
                            }
                            else{
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
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