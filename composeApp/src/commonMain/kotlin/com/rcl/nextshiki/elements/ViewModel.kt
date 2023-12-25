package com.rcl.nextshiki.elements

import kotlinx.coroutines.CoroutineScope

open class ViewModel {

    protected lateinit var viewModelScope: CoroutineScope

    protected fun initialize(scope: CoroutineScope) {
        this.viewModelScope = scope
    }
}