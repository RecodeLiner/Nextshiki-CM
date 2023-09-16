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
                val token = getToken(isFirst = false, code = settings.getString("refCode", ""))
                KtorModel.token.value = token.accessToken!!
                settings["refCode"] = token.refreshToken!!

                val obj = koin.get<KtorRepository>().getCurrentUser()
                settings["id"] = obj!!.id!!
            }

            calendarList.addAll(koin.get<KtorRepository>().getCalendar())
            nearTitle = calendarList[0]

            when (Locale.current.language) {
                "ru" -> previewName = nearTitle.anime!!.russian!!
                "en" -> previewName = nearTitle.anime!!.name!!
            }
        }
    }
}