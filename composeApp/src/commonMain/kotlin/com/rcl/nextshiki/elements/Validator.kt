package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.DOMAIN
import com.rcl.nextshiki.models.universal.Image

fun extractLink(link: String?): String? {
    val regex = Regex("""(?<=src=")(.*?)(?=")""")
    val list = link?.let { regex.find(it) }
    if (link==null || list == null) return null
    return if (list.value.startsWith("//")) {
        "https:${list.value}"
    } else if (list.value.startsWith("/")) {
        "$DOMAIN${list.value}"
    } else {
        list.value
    }
}


fun getValidImageUrl(image: Image): String? {
    return when {
        image.original != null -> {
            getValidUrlByLink(image.original)
        }

        image.x160 != null -> {
            getValidUrlByLink(image.x160)
        }

        else -> {
            null
        }
    }
}

fun getValidUrlByLink(string: String): String {
    return if (string.contains("https://") || string.contains("http://")) {
        string
    } else {
        DOMAIN + string
    }
}
