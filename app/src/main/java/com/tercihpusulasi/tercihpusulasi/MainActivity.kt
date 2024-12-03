package com.tercihpusulasi.tercihpusulasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tercihpusulasi.tercihpusulasi.navigation.BottomNavigationBar
import com.tercihpusulasi.tercihpusulasi.navigation.NavGraph
import com.tercihpusulasi.tercihpusulasi.ui.theme.DarkBlue
import com.tercihpusulasi.tercihpusulasi.ui.theme.TercihPusulasıTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TercihPusulasıTheme {
                window.statusBarColor = DarkBlue.toArgb()

                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

                val navController = rememberNavController()

               Scaffold(
                    bottomBar = {
                        BottomNavigationBar(navController, bottomBarState)
                    }

                ) { paddingValues ->
                    NavGraph(
                        navController = navController,
                        paddingValues = paddingValues,
                        bottomBarState = bottomBarState
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TercihPusulasıTheme {
        Greeting("Android")
    }
}