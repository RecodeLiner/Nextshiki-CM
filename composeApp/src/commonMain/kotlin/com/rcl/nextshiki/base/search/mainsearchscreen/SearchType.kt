package com.rcl.nextshiki.base.search.mainsearchscreen

import com.rcl.mr.MR.strings.search_anime
import com.rcl.mr.MR.strings.search_characters
import com.rcl.mr.MR.strings.search_manga
import com.rcl.mr.MR.strings.search_people
import com.rcl.mr.MR.strings.search_ranobe
import com.rcl.mr.MR.strings.search_users
import dev.icerock.moko.resources.StringResource

enum class SearchType(val stringResource: StringResource, val apiPath: String) {
    Anime(search_anime, "animes"),
    Manga(search_manga, "mangas"),
    Ranobe(search_ranobe, "ranobe"),
    People(search_people, "people"),
    Users(search_users, "users"),
    Characters(search_characters, "characters")
}