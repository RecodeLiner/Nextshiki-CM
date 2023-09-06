package com.rcl.nextshiki.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search

object ScreenList {
    val screens = listOf(
        Routes(
            "Home",
            MainScreen,
            Icons.Outlined.Home,
            Icons.Filled.Home
        ),
        Routes(
            "Search",
            SearchScreen,
            Icons.Outlined.Search,
            Icons.Filled.Search
        ),
        Routes(
            "Profile",
            ProfileScreen,
            Icons.Outlined.AccountCircle,
            Icons.Filled.AccountCircle
        )
    )
}