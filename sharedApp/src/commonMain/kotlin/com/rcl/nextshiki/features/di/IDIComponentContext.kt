package com.rcl.nextshiki.features.di

import com.arkivanov.decompose.GenericComponentContext

interface IDIComponentContext : GenericComponentContext<IDIComponentContext> {
    val globalModule: GlobalAppModule
}