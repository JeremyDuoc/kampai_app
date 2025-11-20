package com.example.kampai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kampai.ui.theme.home.HomeScreen
import com.example.kampai.ui.theme.KampaiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KampaiTheme {
                KampaiApp()
            }
        }
    }
}

@Composable
fun KampaiApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onGameSelected = { route ->
                    if (route == "game_bomb") {
                        navController.navigate("game_bomb")
                    }
                }
            )
        }
        composable("game_bomb") {

        }
    }
}