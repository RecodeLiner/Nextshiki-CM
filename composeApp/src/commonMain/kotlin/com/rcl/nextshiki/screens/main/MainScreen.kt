package com.rcl.nextshiki.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.elements.CalendarCardPreview
import com.rcl.nextshiki.getString

@OptIn(ExperimentalMaterial3Api::class)
object MainScreen : Screen {
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
                    AnimatedVisibility(!vm.calendarList.isEmpty()){

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(220.dp),
                        ) {
                            CalendarCardPreview(vm)
                        }
                    }
                }
            }
        }
    }
}