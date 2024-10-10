package com.rcl.nextshiki.models.searchobject

import com.rcl.nextshiki.models.searchobject.people.Works

interface ISearchParticipation {
    val works: List<Works>
}