package com.rcl.nextshiki.utils

import com.rcl.nextshiki.models.universal.Image

fun extractLink(link: String?, domain: String): String? {
    val regex = Regex("""(?<=src=")(.*?)(?=")""")
    val list = link?.let { regex.find(it) }
    if (link==null || list == null) return null
    return if (list.value.startsWith("//")) {
        "https:${list.value}"
    } else if (list.value.startsWith("/")) {
        "$domain${list.value}"
    } else {
        list.value
    }
}


fun getValidImageUrl(image: Image, domain: String): String? {
    return when {
        image.original != null -> {
            getValidUrlByLink(image.original!!, domain)
        }

        image.x160 != null -> {
            getValidUrlByLink(image.x160!!, domain)
        }

        else -> {
            null
        }
    }
}

fun getValidUrlByLink(string: String, domain: String): String {
    return if (string.contains("https://") || string.contains("http://")) {
        string
    } else {
        domain + string
    }
}
