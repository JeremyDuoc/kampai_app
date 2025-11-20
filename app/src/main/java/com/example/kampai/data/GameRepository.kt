package com.example.kampai.data

import androidx.compose.ui.graphics.Color
import com.example.kampai.domain.models.GameModel
import com.example.kampai.ui.theme.* // Importa todos los colores del tema
import com.example.kampai.R
import javax.inject.Inject

class GameRepository @Inject constructor() {
    fun getGames(): List<GameModel> {
        return listOf(
            GameModel(
                id = "time_bomb",
                title = "La Bomba",
                description = "Pasa el móvil antes de que explote en tus manos.",
                iconRes = R.drawable.ic_bomb,
                color = AccentRed,
                route = "game_bomb"
            ),
            GameModel(
                id = "never_have_i_ever",
                title = "Yo Nunca Nunca",
                description = "Si lo has hecho, bebes. Secretos salen a la luz.",
                iconRes = R.drawable.ic_users,
                color = AccentCyan,
                route = "game_never"
            ),
            GameModel(
                id = "truth_or_dare",
                title = "Verdad o Reto",
                description = "¿Te atreves o confiesas? La presión sube.",
                iconRes = R.drawable.ic_flame,
                color = AccentAmber,
                route = "game_truth"
            ),
            GameModel(
                id = "culture",
                title = "Cultura Chupística",
                description = "Nombra elementos de una categoría sin repetir.",
                iconRes = R.drawable.ic_users, // Reutilizamos icono de usuarios
                color = PrimaryViolet,
                route = "game_culture"
            ),
            GameModel(
                id = "high_low",
                title = "Mayor o Menor",
                description = "Adivina si la próxima carta es más alta o baja.",
                iconRes = R.drawable.ic_flame, // Reutilizamos icono de fuego
                color = SecondaryPink,
                route = "game_highlow"
            ),
            GameModel(
                id = "medusa",
                title = "La Medusa",
                description = "No cruces miradas o bebes.",
                iconRes = R.drawable.ic_users, // Reutilizamos icono de usuarios
                color = AccentCyan,
                route = "game_medusa"
            ),
            GameModel(
                id = "charades",
                title = "Mímica Borracha",
                description = "Actúa sin hablar. Tu equipo debe adivinar.",
                iconRes = R.drawable.ic_users, // Reutilizamos icono de usuarios
                color = AccentAmber,
                route = "game_charades"
            ),
            GameModel(
                id = "roulette",
                title = "Ruleta Rusa",
                description = "6 cámaras, 1 disparo. ¿Tendrás suerte?",
                iconRes = R.drawable.ic_bomb, // Reutilizamos icono de bomba
                color = AccentRed,
                route = "game_roulette"
            ),
            GameModel(
                id = "judge",
                title = "El Juez",
                description = "Obedece la regla absurda o bebe.",
                iconRes = R.drawable.ic_users, // Reutilizamos icono de usuarios
                color = AccentAmber,
                route = "game_judge"
            ),
            GameModel(
                id = "staring",
                title = "Duelo de Miradas",
                description = "El primero en parpadear pierde.",
                iconRes = R.drawable.ic_users, // Reutilizamos icono de usuarios
                color = PrimaryViolet,
                route = "game_staring"
            )
        )
    }
}