package com.rcl.nextshiki.components

import kotlinx.coroutines.flow.StateFlow

interface IWebUri {
    val currentLink: StateFlow<String>
}