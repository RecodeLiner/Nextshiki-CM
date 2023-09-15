@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.rcl.nextshiki.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.getString

object SearchScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var text by remember { mutableStateOf("") }
        var selected by remember { mutableStateOf("") }
        selected = getString(search_anime)
        val vm = rememberScreenModel { SearchViewModel() }


        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Scaffold {
            Box(modifier = Modifier.padding(it)){
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 10.dp),
                        leadingIcon = { Icon(Icons.Default.Search, "Search icon") },
                        trailingIcon = { Icon(Icons.Default.MoreVert, "Vertical menu") },
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = { Text(getString(MR.strings.search_example)) }
                    )
                    LazyRow {

                    }
                    LazyColumn {  }
                }
            }
        }
    }
}