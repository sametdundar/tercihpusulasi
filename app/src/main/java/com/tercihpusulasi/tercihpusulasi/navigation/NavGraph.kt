package com.tercihpusulasi.tercihpusulasi.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sametdundar.guaranteeapp.roomdatabase.FormData
import com.tercihpusulasi.tercihpusulasi.ui.screens.ContinueScreen
import com.tercihpusulasi.tercihpusulasi.ui.screens.ListScreen
import com.tercihpusulasi.tercihpusulasi.ui.screens.ShareScreen
import com.tercihpusulasi.tercihpusulasi.ui.screens.UserDetailsScreen
import com.tercihpusulasi.tercihpusulasi.utils.JsonConverter.fromJson

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    bottomBarState: MutableState<Boolean>
) {

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Home.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable(route = ScreenRoutes.Home.route) {
            bottomBarState.value = true
            ListScreen(navController = navController)
        }

        composable(route = ScreenRoutes.Favorites.route) {
            bottomBarState.value = true
            ShareScreen(navController = navController)
        }

        composable(route = ScreenRoutes.Messages.route) {
            bottomBarState.value = true
            ContinueScreen()
        }

        composable(route = ScreenRoutes.FormDetail.route+"?formData={formData}") { backStackEntry ->
            bottomBarState.value = false
            val formData =  backStackEntry.arguments?.getString("formData")
            val form = fromJson<FormData>(formData?:"")
            UserDetailsScreen(formData = form, navController = navController)

        }




    }
}