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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.contentscreens.AnimeScreen
import com.rcl.nextshiki.elements.contentscreens.CharacterScreen
import com.rcl.nextshiki.elements.contentscreens.MangaScreen
import com.rcl.nextshiki.elements.contentscreens.PeopleScreen
import com.rcl.nextshiki.elements.contentscreens.RanobeScreen
import com.rcl.nextshiki.elements.contentscreens.UserScreen
import com.rcl.nextshiki.locale.CustomLocale.getLangRes
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SimpleSearchModel
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import com.rcl.nextshiki.models.searchobject.characters.CharacterModel
import com.rcl.nextshiki.models.searchobject.manga.MangaObject
import com.rcl.nextshiki.models.searchobject.people.PeopleObject
import com.rcl.nextshiki.models.searchobject.ranobe.RanobeObject
import com.rcl.nextshiki.models.searchobject.users.UserObject

@Composable
fun SearchedElementComponentScreen(searchComponent: SearchedElementComponent) {
    val searchedElement by searchComponent.searchedElement.subscribeAsState()

    Scaffold(
        topBar = { TopAppBar(searchComponent, searchedElement) },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).padding(horizontal = 10.dp)) {
                if (searchedElement !is SimpleSearchModel) {
                    DisplaySearchedElement(searchedElement, searchComponent::navigateTo)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(searchComponent: SearchedElementComponent, searchedElement: CommonSearchInterface) {
    CenterAlignedTopAppBar(
        title = { TitleText(searchedElement) },
        navigationIcon = { NavigationIcon(searchComponent) }
    )
}

@Composable
fun TitleText(searchedElement:  CommonSearchInterface) {
    getLangRes(
        russian = searchedElement.russian,
        english = searchedElement.name
    )?.let {
        Text(
            maxLines = 2,
            text = it,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NavigationIcon(searchComponent: SearchedElementComponent) {
    IconButton(onClick = searchComponent::popBack) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back from content"
        )
    }
}

@Composable
fun DisplaySearchedElement(searchedElement:  CommonSearchInterface, navigateTo: (String, SearchType) -> Unit) {
    when (searchedElement) {
        is AnimeObject -> AnimeScreen(searchedElement, navigateTo)
        is MangaObject -> MangaScreen(searchedElement, navigateTo)
        is RanobeObject -> RanobeScreen(searchedElement, navigateTo)
        is UserObject -> UserScreen(searchedElement, navigateTo)
        is PeopleObject -> PeopleScreen(searchedElement, navigateTo)
        is CharacterModel -> CharacterScreen(searchedElement, navigateTo)
    }
}
