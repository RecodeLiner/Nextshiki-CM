package com.rcl.nextshiki.base.search.mainsearchscreen

import com.rcl.nextshiki.locale.LocalizedString

enum class SearchType(val getRes: (LocalizedString) -> String, val apiPath: String) {
    Anime({ it.search_anime }, "animes"),
    Manga({ it.search_manga }, "mangas"),
    Ranobe({ it.search_ranobe }, "ranobe"),
    People({ it.search_people }, "people"),
    Users({ it.search_users }, "users"),
    Characters({ it.search_characters }, "characters")
}