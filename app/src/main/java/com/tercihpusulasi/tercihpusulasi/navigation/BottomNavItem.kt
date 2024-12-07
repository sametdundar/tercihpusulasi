package com.tercihpusulasi.tercihpusulasi.navigation

import com.tercihpusulasi.tercihpusulasi.R

sealed class BottomNavItem(
    val title: String,
    val image: Int,
    val route: String
) {
    data object Home : BottomNavItem(
        title = "Üniversiteler",
        image = R.drawable.ic_home,
        route = ScreenRoutes.Home.route
    )

    data object Favorites : BottomNavItem(
        title = "Bölümler",
        image = R.drawable.ic_favorites,
        route = ScreenRoutes.Favorites.route
    )

    data object Messages : BottomNavItem(
        title = "Tercih Listem",
        image = R.drawable.ic_messages,
        route = ScreenRoutes.Messages.route
    )
}