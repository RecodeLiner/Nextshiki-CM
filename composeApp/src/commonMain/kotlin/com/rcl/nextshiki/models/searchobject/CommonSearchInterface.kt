package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.universal.Image

interface CommonSearchInterface {
    val id: Int?
    val name: String?
    val russian: String?
    val url: String?
    val image: Image?
}
