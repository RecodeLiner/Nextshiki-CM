package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.SharedRes
import dev.icerock.moko.resources.StringResource

enum class SearchType(val stringResource: StringResource, val apiPath: String) {
    Anime(stringResource = SharedRes.strings.search_anime, apiPath = "animes"),
    Manga(stringResource = SharedRes.strings.search_manga, apiPath = "mangas"),
    Ranobe(stringResource = SharedRes.strings.search_ranobe, apiPath = "ranobe"),
    People(stringResource = SharedRes.strings.search_people, apiPath = "people"),
    Users(stringResource = SharedRes.strings.search_users, apiPath = "users"),
    Characters(stringResource = SharedRes.strings.search_characters, apiPath = "characters")
}
