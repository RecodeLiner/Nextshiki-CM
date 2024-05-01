package com.rcl.nextshiki.base.search.mainsearchscreen

import com.rcl.nextshiki.locale.LocalizedString

enum class SearchType(val getRes: (LocalizedString) -> String) {
    Anime({ it.search_anime }),
    Manga({ it.search_manga }),
    Ranobe({ it.search_ranobe }),
    People({ it.search_people }),
    Users({ it.search_users }),
    Characters({ it.search_characters })
}