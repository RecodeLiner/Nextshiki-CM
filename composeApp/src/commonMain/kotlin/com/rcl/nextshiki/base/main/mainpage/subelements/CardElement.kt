package com.rcl.nextshiki.base.main.mainpage.subelements

data class CardElement(
    override var name: String = "",
    override var imageLink: String = "",
    override var nextEpisodeAt: String = ""
) : ICard