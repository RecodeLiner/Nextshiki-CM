package com.rcl.nextshiki.components

import HomeFeatureModule
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.rcl.nextshiki.components.factories.HomeDependencies
import com.rcl.nextshiki.components.factories.HomeStoreFactory
import com.rcl.nextshiki.domain.usecases.GetCalendarUseCase
import com.rcl.nextshiki.features.di.IDIComponentContext

class HomeComponent(
    componentContext: IDIComponentContext
) : IDIComponentContext by componentContext {
    val homeFeatureModule by lazy {
        HomeFeatureModule(globalModule)
    }
    val store by lazy {
        instanceKeeper.getStore {
            HomeStoreFactory(
                storeFactory = DefaultStoreFactory(),
                homeDependencies =
                    object : HomeDependencies {
                        override val getCalendarUseCase: GetCalendarUseCase
                            get() = homeFeatureModule.getCalendarUseCase
                    },
            ).create()
        }
    }
}