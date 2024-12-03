package com.tercihpusulasi.tercihpusulasi.navigation

import com.tercihpusulasi.tercihpusulasi.R

sealed class BottomNavItem(
    val title: String,
    val image: Int,
    val route: String
) {
    data object Home : BottomNavItem(
        title = "Listele",
        image = R.drawable.ic_home,
        route = ScreenRoutes.Home.route
    )

    data object Favorites : BottomNavItem(
        title = "Paylaş",
        image = R.drawable.ic_favorites,
        route = ScreenRoutes.Favorites.route
    )

    data object Messages : BottomNavItem(
        title = "Devam Et",
        image = R.drawable.ic_messages,
        route = ScreenRoutes.Messages.route
    )
}