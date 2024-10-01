package com.rcl.nextshiki.elements

import com.rcl.nextshiki.di.clipboard.IClipboard
import com.rcl.nextshiki.di.ktor.KtorModuleObject.networkModule
import com.rcl.nextshiki.di.settings.SettingsModuleObject.settingsModule
import org.koin.core.module.Module
import org.koin.dsl.module

fun setupKoin(clipboard: IClipboard) : Array<Module> {
    return arrayOf(networkModule, settingsModule, module{ single<IClipboard> { clipboard } })
}
