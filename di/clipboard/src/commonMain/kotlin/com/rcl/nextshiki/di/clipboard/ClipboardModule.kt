package com.rcl.nextshiki.di.clipboard

import org.koin.dsl.module

object ClipboardModule {
    val clipboardModule = module {
        single<IClipboard> {
            ClipboardImpl()
        }
    }
}