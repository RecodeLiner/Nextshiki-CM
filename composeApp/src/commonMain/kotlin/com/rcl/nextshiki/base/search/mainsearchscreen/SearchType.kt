package com.rcl.nextshiki.base.search.mainsearchscreen

import com.rcl.mr.MR.strings.search_anime
import com.rcl.mr.MR.strings.search_characters
import com.rcl.mr.MR.strings.search_manga
import com.rcl.mr.MR.strings.search_people
import com.rcl.mr.MR.strings.search_ranobe
import com.rcl.mr.MR.strings.search_users
import dev.icerock.moko.resources.StringResource

enum class SearchType(val stringResource: StringResource, val apiPath: String) {
    Anime(stringResource = search_anime, apiPath = "animes"),
    Manga(stringResource = search_manga, apiPath = "mangas"),
    Ranobe(stringResource = search_ranobe, apiPath = "ranobe"),
    People(stringResource = search_people, apiPath = "people"),
    Users(stringResource = search_users, apiPath = "users"),
    Characters(stringResource = search_characters, apiPath = "characters")
}