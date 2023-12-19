package com.rcl.nextshiki.base.main

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.base.main.subelements.CardElement

interface IMain {
    val cardElement: Value<CardElement>
}