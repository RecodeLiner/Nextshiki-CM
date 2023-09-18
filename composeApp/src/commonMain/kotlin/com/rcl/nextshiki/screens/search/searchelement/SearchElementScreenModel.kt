package com.rcl.nextshiki.screens.search.searchelement

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.moe.ResultModel
import com.rcl.nextshiki.models.searchobject.ObjById
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class SearchElementScreenModel(val type: String, val id: String) : ScreenModel {
    val currObj = mutableStateOf<ObjById?>(null)
    val titleName = mutableStateOf("")
    val listForAnimeContent = mutableStateListOf<ResultModel>()
    init {
        coroutineScope.launch {
            currObj.value = koin.get<KtorRepository>().getObjectById(type = type, id = id)
            when (Locale.current.language) {
                "ru" -> titleName.value = currObj.value!!.russian!!
                "en" -> titleName.value = currObj.value!!.name!!
            }
        }
    }

    fun copyContent(clipboard: ClipboardManager, text: String) {
        clipboard.setText(AnnotatedString(text))
    }
    fun getLinks() {
        coroutineScope.launch {
            val list = koin.get<KtorRepository>().getVideoLinks(currObj.value!!.id!!.toString()).result
            listForAnimeContent.addAll(list)
            Napier.i(list.toString())
        }
    }
}
