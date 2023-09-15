package com.rcl.nextshiki.screens.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.calendar.CalendarModel
import com.rcl.nextshiki.screens.main.MainScreenObject.calendar
import com.rcl.nextshiki.screens.main.MainScreenObject.currModel
import kotlinx.coroutines.launch


class MainViewModel : ScreenModel {
    val calendarList = mutableStateListOf<CalendarModel>()
    lateinit var nearTitle: CalendarModel
    lateinit var previewName: String
    init{
        if (calendar.isNotEmpty()) {
            calendarList.addAll(calendar)
            nearTitle = currModel
            when (Locale.current.language) {
                "ru" -> previewName = nearTitle.anime!!.russian!!
                "en" -> previewName = nearTitle.anime!!.name!!
            }
        }
        coroutineScope.launch {
            calendar.addAll(koin.get<KtorRepository>().getCalendar())
            currModel = calendar[0]
            calendarList.addAll(calendar)
            nearTitle = currModel

            when (Locale.current.language) {
                "ru" -> previewName = nearTitle.anime!!.russian!!
                "en" -> previewName = nearTitle.anime!!.name!!
            }
        }
    }
}