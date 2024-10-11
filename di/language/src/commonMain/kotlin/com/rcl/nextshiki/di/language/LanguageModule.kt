package com.rcl.nextshiki.di.language

import org.koin.dsl.module

object LanguageModule {
    val langModule = module {
        single {
            LanguageRepo(get())
        }
    }
}