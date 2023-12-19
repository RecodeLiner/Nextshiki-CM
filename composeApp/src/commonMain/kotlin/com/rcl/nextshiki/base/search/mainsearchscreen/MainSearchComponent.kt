package com.rcl.nextshiki.base.search.mainsearchscreen

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class MainSearchComponent : IMainSearch {
    private val _text = MutableValue("")
    override var text: Value<String> = _text
    override fun onTextChanged(value: String) {
        _text.value = value
    }
}