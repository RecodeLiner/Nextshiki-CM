package com.rcl.nextshiki.base.main.mainpage

import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement
import com.rcl.nextshiki.models.topics.HotTopics

interface IMain {
    val cardList: MutableList<CardElement>
    val topicsList: MutableList<HotTopics>
}