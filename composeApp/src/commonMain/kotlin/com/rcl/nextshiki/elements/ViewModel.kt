package com.rcl.nextshiki.elements

import kotlinx.coroutines.CoroutineScope

open class ViewModel {

    private lateinit var viewModelScope: CoroutineScope

    fun initialize(scope: CoroutineScope) {
        this.viewModelScope = scope
    }
}