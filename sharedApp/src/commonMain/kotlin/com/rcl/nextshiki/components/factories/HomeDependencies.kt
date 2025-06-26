package com.rcl.nextshiki.components.factories

import com.rcl.nextshiki.domain.usecases.GetCalendarUseCase

interface HomeDependencies {
    val getCalendarUseCase: GetCalendarUseCase
}