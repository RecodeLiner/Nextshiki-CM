package com.rcl.nextshiki.base.main.mainpage.subelements

data class CardElement(
    override val id: Int = 0,
    override val name: String = "",
    override val imageLink: String = "",
    override val nextEpisodeAt: String = ""
) : ICard