package com.rcl.nextshiki.base.main.mainpage

import com.arkivanov.decompose.value.Value
import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement
import com.rcl.nextshiki.models.topics.HotTopics

interface IMain {
    val cardElement: Value<CardElement>
    val topicsList: MutableList<HotTopics>
}