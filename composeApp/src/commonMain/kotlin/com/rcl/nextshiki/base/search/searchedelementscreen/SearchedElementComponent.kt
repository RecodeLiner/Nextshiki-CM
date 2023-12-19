package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.searchobject.ObjById
import com.rcl.nextshiki.models.searchobject.SearchDataModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchedElementComponent(
    data: SearchDataModel,
    context: ComponentContext,
    searchContext: CoroutineContext
) : ComponentContext by context, ISearchedElement {
    private val _searchedElement = MutableValue(ObjById())
    override val searchedElement: Value<ObjById> = _searchedElement

    private val coroutine = coroutineScope(searchContext + SupervisorJob())

    init {
        context.lifecycle.doOnCreate {
            coroutine.launch{
                _searchedElement.value = koin.get<KtorRepository>().getObjectById(type = data.type, id = data.id)
            }
        }
    }
}