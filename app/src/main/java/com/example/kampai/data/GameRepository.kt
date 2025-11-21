package com.example.kampai.data

import androidx.compose.ui.graphics.Color
import com.example.kampai.domain.models.GameModel
import com.example.kampai.ui.theme.* // Importa todos los colores del tema
import com.example.kampai.R
import javax.inject.Inject

class GameRepository @Inject constructor() {
    fun getGames(): List<GameModel> {
        return listOf(
            // CULTURA CHUPÍSTICA (CONSOLIDADOS CULTURA + BOMBA)
            GameModel(
                id = "culture",
                title = "Cultura Chupística",
                description = "Modo Clásico o Bomba. Elige el desafío de velocidad.",
                iconRes = R.drawable.culture, // <--- Icono PNG
                color = PrimaryViolet,
                route = "culture_selection" // Rota para la pantalla de selección de modo
            ),

            GameModel(
                id = "never_have_i_ever",
                title = "Yo Nunca Nunca",
                description = "Si lo has hecho, bebes. Secretos picantes revelados.",
                iconRes = R.drawable.never, // <--- Icono PNG
                color = AccentCyan,
                route = "game_never"
            ),
            GameModel(
                id = "truth_or_dare",
                title = "Verdad o Reto",
                description = "¿Te atreves o confiesas? La presión sube.",
                iconRes = R.drawable.truth, // <--- Icono PNG
                color = AccentAmber,
                route = "game_truth"
            ),
            GameModel(
                id = "high_low",
                title = "Mayor o Menor",
                description = "Adivina si la próxima carta es más alta o baja.",
                iconRes = R.drawable.highlow, // <--- Icono PNG
                color = SecondaryPink,
                route = "game_highlow"
            ),
            GameModel(
                id = "medusa",
                title = "La Medusa",
                description = "No cruces miradas o bebes.",
                iconRes = R.drawable.medusa, // <--- Icono PNG
                color = AccentCyan,
                route = "game_medusa"
            ),
            GameModel(
                id = "charades",
                title = "Mímica Borracha",
                description = "Actúa sin hablar. Tu equipo debe adivinar.",
                iconRes = R.drawable.mimic, // <--- Icono PNG
                color = AccentAmber,
                route = "game_charades"
            ),
            GameModel(
                id = "roulette",
                title = "Ruleta Rusa",
                description = "6 cámaras, 1 disparo. ¿Tendrás suerte?",
                iconRes = R.drawable.ruleta, // <--- Icono PNG
                color = AccentRed,
                route = "game_roulette"
            ),
            GameModel(
                id = "judge",
                title = "El Juez",
                description = "Obedece la regla absurda o bebe.",
                iconRes = R.drawable.juez,
                color = AccentAmber,
                route = "game_judge"
            ),
            GameModel(
                id = "staring",
                title = "Duelo de Miradas",
                description = "El primero en parpadear pierde.",
                iconRes = R.drawable.duelo,
                color = PrimaryViolet,
                route = "game_staring"
            ),
            GameModel(
                id = "most_likely",
                title = "¿Quién es más probable?",
                description = "Señalen a la persona más probable. El más votado bebe.",
                iconRes = R.drawable.truth, // Usa un icono temporal
                color = Color(0xFFE100FF), // SecondaryPink
                route = "game_likely"
            ),

            GameModel(
                id = "warmup",
                title = "PartyMix",
                description = "Muchos retos y minijuegos para borrarte con tus amigos",
                iconRes = R.drawable.warmup,
                color = Color(0xFFF59E0B),
                route = "game_warmup"
            )
        )
    }
}