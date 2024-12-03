package com.tercihpusulasi.tercihpusulasi.navigation

import com.tercihpusulasi.tercihpusulasi.utils.Constants.CONTINUE_SCREEN
import com.tercihpusulasi.tercihpusulasi.utils.Constants.FORM_DETAIL
import com.tercihpusulasi.tercihpusulasi.utils.Constants.LIST_SCREEN
import com.tercihpusulasi.tercihpusulasi.utils.Constants.SHARE_SCREEN

sealed class ScreenRoutes(val route : String) {
    data object Home : ScreenRoutes(LIST_SCREEN)
    data object Favorites : ScreenRoutes(SHARE_SCREEN)
    data object Messages : ScreenRoutes(CONTINUE_SCREEN)
    data object FormDetail : ScreenRoutes(FORM_DETAIL)
}