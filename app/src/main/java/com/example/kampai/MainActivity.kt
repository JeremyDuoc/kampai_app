package com.example.kampai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kampai.ui.theme.KampaiTheme
import com.example.kampai.ui.theme.home.HomeScreen
import com.example.kampai.ui.theme.bomb.BombGameScreen
import com.example.kampai.ui.theme.never.NeverGameScreen
import com.example.kampai.ui.theme.truth.TruthGameScreen
import com.example.kampai.ui.theme.culture.CultureGameScreen
import com.example.kampai.ui.theme.highlow.HighLowGameScreen
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

    NavHost(
        navController = navController,
        startDestination = "home",
        // Animaciones de transición para una experiencia más fluida
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) }
    ) {
        // 1. Menú Principal
        composable("home") {
            HomeScreen(
                onGameSelected = { route ->
                    navController.navigate(route)
                }
            )
        }

        // 2. Juego: La Bomba
        composable("game_bomb") {
            BombGameScreen(onBack = { navController.popBackStack() })
        }

        // 3. Juego: Yo Nunca Nunca
        composable("game_never") {
            NeverGameScreen(onBack = { navController.popBackStack() })
        }

        // 4. Juego: Verdad o Reto
        composable("game_truth") {
            TruthGameScreen(onBack = { navController.popBackStack() })
        }

        // 5. Juego: Cultura Chupística
        composable("game_culture") {
            CultureGameScreen(onBack = { navController.popBackStack() })
        }

        // 6. Juego: Mayor o Menor
        composable("game_highlow") {
            HighLowGameScreen(onBack = { navController.popBackStack() })
        }
    }
}