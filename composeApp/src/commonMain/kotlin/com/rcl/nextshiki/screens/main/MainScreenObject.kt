package com.rcl.nextshiki.screens.main

import com.rcl.nextshiki.models.calendar.CalendarModel

object MainScreenObject {
    val calendar = mutableListOf<CalendarModel>()
    lateinit var currModel: CalendarModel
}