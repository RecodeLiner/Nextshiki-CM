package com.rcl.nextshiki.base.search.searchedelementscreen

import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import com.rcl.nextshiki.elements.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.elements.contentscreens.AnimeScreen
import com.rcl.nextshiki.elements.contentscreens.CharacterScreen
import com.rcl.nextshiki.elements.contentscreens.MangaScreen
import com.rcl.nextshiki.elements.contentscreens.PeopleScreen
import com.rcl.nextshiki.elements.contentscreens.RanobeScreen
import com.rcl.nextshiki.elements.contentscreens.UserScreen
import com.rcl.nextshiki.elements.withLocalSharedTransition
import com.rcl.nextshiki.locale.CustomLocale.getLangRes
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel

@Composable
fun SearchedElementComponentScreen(searchComponent: SearchedElementComponent) {
    val searchedElement by searchComponent.searchedElement.subscribeAsState()

    Scaffold(
        topBar = { TopAppBar(searchComponent, searchedElement) },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).padding(horizontal = 10.dp)) {
                DisplaySearchedElement(searchedElement, searchComponent.contentType, searchComponent::navigateTo)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(searchComponent: SearchedElementComponent, searchedElement: CommonSearchInterface) {
    CenterAlignedTopAppBar(
        title = { TitleText(searchedElement) },
        navigationIcon = { NavigationIcon(searchComponent) }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TitleText(searchedElement: CommonSearchInterface) {
    getLangRes(
        russian = searchedElement.russian,
        english = searchedElement.name
    )?.let {
        withLocalSharedTransition {
            Text(
                maxLines = 2,
                text = it,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState("searched_card_${searchedElement.id}_name"),
                    LocalAnimatedVisibilityScope.current
                )
            )
        }
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
fun DisplaySearchedElement(searchedElement: CommonSearchInterface, contentType: SearchType, navigateTo: (SearchCardModel, SearchType) -> Unit) {
    when (contentType) {
        SearchType.Anime -> AnimeScreen(searchedElement, navigateTo)
        SearchType.Manga -> MangaScreen(searchedElement, navigateTo)
        SearchType.Ranobe -> RanobeScreen(searchedElement, navigateTo)
        SearchType.People -> UserScreen(searchedElement, navigateTo)
        SearchType.Users -> PeopleScreen(searchedElement, navigateTo)
        SearchType.Characters -> CharacterScreen(searchedElement, navigateTo)
    }
}
