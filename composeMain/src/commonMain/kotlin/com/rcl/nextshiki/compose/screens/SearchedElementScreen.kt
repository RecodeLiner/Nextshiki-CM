package com.rcl.nextshiki.compose.screens

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
import com.rcl.nextshiki.components.searchcomponent.searchedelementscreen.SearchedElementComponent
import com.rcl.nextshiki.compose.LocalAnimatedVisibilityScope
import com.rcl.nextshiki.compose.getLangRes
import com.rcl.nextshiki.compose.screens.content.SearchedContentCommon
import com.rcl.nextshiki.compose.withLocalSharedTransition
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType

@Composable
fun SearchedElementScreen(searchedElementComponent: SearchedElementComponent) {
    val vm = searchedElementComponent.vm
    val currentCode by vm.languageRepo.localeVar.subscribeAsState()
    val searchedElement by vm.searchedElement.subscribeAsState()

    Scaffold(
        topBar = {
            searchedElement.name?.let {
                searchedElement.russian?.let { russian ->
                    CustomTopAppBar(
                        name = getLangRes(
                            currentCode = currentCode,
                            russian = russian,
                            english = it,
                        ),
                        id = searchedElement.id,
                        popBack = searchedElementComponent::popBack
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).padding(horizontal = 10.dp)) {
                DisplaySearchedElement(
                    currentCode,
                    searchedElement,
                    vm.contentType,
                    searchedElementComponent::navigateTo
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun CustomTopAppBar(
    name: String,
    id: Int?,
    popBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            withLocalSharedTransition {
                Text(
                    maxLines = 2,
                    text = name,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState("searched_card_${id}_name"),
                        LocalAnimatedVisibilityScope.current
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = popBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back from content"
                )
            }
        }
    )
}

@Composable
fun DisplaySearchedElement(
    currentCode: String,
    searchedElement: CommonSearchInterface,
    contentType: SearchType,
    navigateTo: (SearchCardModel, SearchType) -> Unit
) {
    SearchedContentCommon(
        searchType = contentType,
        currentCode = currentCode,
        data = searchedElement,
        navigateTo = navigateTo
    )
}