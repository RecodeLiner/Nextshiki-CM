package com.rcl.nextshiki.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.screens.main.MainScreen
import com.rcl.nextshiki.screens.profile.ProfileScreen
import com.rcl.nextshiki.screens.search.SearchScreen

object ScreenList {
    val screens = listOf(
        Routes(
            name = MR.strings.bottom_main,
            screen = MainScreen(),
            outlinedIcon = Icons.Outlined.Home,
            filledIcon = Icons.Filled.Home
        ),
        Routes(
            name = MR.strings.bottom_search,
            screen = SearchScreen(),
            outlinedIcon = Icons.Outlined.Search,
            filledIcon = Icons.Filled.Search
        ),
        Routes(
            name = MR.strings.bottom_profile,
            screen = ProfileScreen,
            outlinedIcon = Icons.Outlined.AccountCircle,
            filledIcon = Icons.Filled.AccountCircle
        )
    )
}