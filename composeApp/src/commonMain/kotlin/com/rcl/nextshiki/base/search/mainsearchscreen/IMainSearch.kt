package com.rcl.nextshiki.base.search.mainsearchscreen

import com.arkivanov.decompose.value.Value

interface IMainSearch {
    var text: Value<String>
    fun onTextChanged(value: String)
}