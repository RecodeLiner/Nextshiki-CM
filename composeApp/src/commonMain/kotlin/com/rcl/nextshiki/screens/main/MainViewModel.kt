package com.rcl.nextshiki.screens.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.getToken
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.calendar.CalendarModel
import com.rcl.nextshiki.navEnabled
import com.rcl.nextshiki.settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.set
import kotlinx.coroutines.launch


class MainViewModel : ScreenModel {
    val calendarList = mutableStateListOf<CalendarModel>()
    lateinit var nearTitle: CalendarModel
    lateinit var previewName: String
    init{
        coroutineScope.launch {
            if (settings.contains("refCode")){
                navEnabled.value = false
                updateToken()
            }
            calendarList.addAll(koin.get<KtorRepository>().getCalendar())
            nearTitle = calendarList[0]

            previewName = when (Locale.current.language) {
                "ru" -> nearTitle.anime!!.russian!!
                else -> nearTitle.anime!!.name!!
            }
        }
    }
    private fun updateToken(){
        coroutineScope.launch {
            val token = getToken(isFirst = false, code = settings.getString("refCode", ""))
            KtorModel.token.value = token.accessToken!!
            KtorModel.scope.value = token.scope!!
            settings["refCode"] = token.refreshToken!!

            val obj = koin.get<KtorRepository>().getCurrentUser()
            settings["id"] = obj!!.id!!
            navEnabled.value = true
        }
    }
}