package com.rcl.nextshiki

import com.rcl.nextshiki.di.ktor.KtorModel
import kotlinx.coroutines.runBlocking
import org.koin.core.Koin
import org.koin.core.context.startKoin

object Koin{
    lateinit var koin: Koin

    fun setupKoin(){
        if (!this::koin.isInitialized)
        {
            runBlocking {
                koin = startKoin { modules(KtorModel.networkModule) }.koin
            }
        }
    }
}