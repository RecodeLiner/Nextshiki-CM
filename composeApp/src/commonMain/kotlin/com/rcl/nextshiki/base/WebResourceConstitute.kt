package com.rcl.nextshiki.base

import com.arkivanov.decompose.value.MutableValue

interface WebResourceConstitute {
    val webUri: MutableValue<String>?
}