package com.rcl.nextshiki.base.search.searchedelementscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.elements.contentscreens.AnimeScreen
import com.rcl.nextshiki.models.searchobject.SimpleSearchModel
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import com.rcl.nextshiki.models.searchobject.manga.MangaObject
import com.rcl.nextshiki.models.searchobject.people.PeopleObject
import com.rcl.nextshiki.models.searchobject.ranobe.RanobeObject
import com.rcl.nextshiki.models.searchobject.users.UserObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchedElementComponentScreen(searchComponent: SearchedElementComponent) {
    val searchedElement by searchComponent.searchedElement.subscribeAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    when (Locale.current.language) {
                        "ru" -> searchedElement.russian ?: searchedElement.name
                        else -> searchedElement.name
                    }?.let {
                        Text(
                            text = it
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        searchComponent.popBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back from content"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (searchedElement !is SimpleSearchModel) {
                when (searchedElement) {
                    is AnimeObject -> {
                        AnimeScreen(searchedElement as AnimeObject)
                    }

                    is MangaObject -> {

                    }

                    is RanobeObject -> {

                    }

                    is UserObject -> {

                    }

                    is PeopleObject -> {

                    }
                }
            }
        }
    }
}