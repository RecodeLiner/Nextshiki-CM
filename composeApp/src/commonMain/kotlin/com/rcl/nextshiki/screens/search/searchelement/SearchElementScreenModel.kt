package com.rcl.nextshiki.screens.search.searchelement

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.searchobject.ObjById
import kotlinx.coroutines.launch

class SearchElementScreenModel(type: String, id: String) : ScreenModel {
    val CurrObj = mutableStateOf<ObjById?>(null)
    val titleName = mutableStateOf("")
    init {
        coroutineScope.launch {
            CurrObj.value = koin.get<KtorRepository>().getObjectById(type = type, id = id)
            when (Locale.current.language) {
                "ru" -> titleName.value = CurrObj.value!!.russian!!
                "en" -> titleName.value = CurrObj.value!!.name!!
            }
        }
    }
}