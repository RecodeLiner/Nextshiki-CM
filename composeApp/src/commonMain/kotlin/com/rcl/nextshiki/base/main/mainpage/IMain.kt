package com.rcl.nextshiki.base.main.mainpage

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement

interface IMain {
    val cardElement: Value<CardElement>
}