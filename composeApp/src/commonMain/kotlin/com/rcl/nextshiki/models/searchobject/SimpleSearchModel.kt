package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.universal.Image

data class SimpleSearchModel(
    override val id: Int? = null,
    override val name: String? = null,
    override val russian: String? = null,
    override val url: String? = null,
    override val image: Image? = null
) : CommonSearchInterface
