package com.rcl.nextshiki.di

import com.rcl.nextshiki.di.ktor.KtorModel.networkModule
import org.koin.core.Koin
import org.koin.core.context.startKoin

object Koin {
    lateinit var koin: Koin

    fun setupKoin() {
        if (!this::koin.isInitialized) {
            koin = startKoin { modules(networkModule) }.koin
        }
    }

    fun getSafeKoin() : Koin {
        if(!this::koin.isInitialized){
            setupKoin()
        }
        return koin
    }
}