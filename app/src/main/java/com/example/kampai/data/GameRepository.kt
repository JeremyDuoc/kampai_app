package com.example.kampai.data

import com.example.kampai.domain.models.GameModel
import com.example.kampai.ui.theme.*
import com.example.kampai.R
import javax.inject.Inject

class GameRepository @Inject constructor() {
    fun getGames(): List<GameModel> {
        return listOf(
            // 1. "La Bomba" ELIMINADA de aquí (ahora será un modo dentro de Cultura)

            // 2. "Cultura Chupística" ACTUALIZADA
            GameModel(
                id = "culture",
                title = "Cultura Chupística",
                // Actualizamos la descripción para reflejar los dos modos
                description = "Modo Clásico o Bomba. ¡Tú eliges el castigo!",
                // --- AQUÍ CAMBIAS EL ICONO ---
                // Cambia 'ic_users' por el nombre de tu nuevo icono en drawable
                iconRes = R.drawable.culture,
                color = PrimaryViolet,
                // Cambiamos la ruta para ir a la selección de modo en lugar del juego directo
                route = "culture_selection"
            ),

            // ... (El resto de juegos se mantienen igual) ...

            GameModel(
                id = "never_have_i_ever",
                title = "Yo Nunca Nunca",
                description = "Si lo has hecho, bebes. Secretos salen a la luz.",
                iconRes = R.drawable.never, // <--- Cambia este icono si quieres
                color = AccentCyan,
                route = "game_never"
            ),
            GameModel(
                id = "truth_or_dare",
                title = "Verdad o Reto",
                description = "¿Te atreves o confiesas? La presión sube.",
                iconRes = R.drawable.truth, // <--- Cambia este icono si quieres
                color = AccentAmber,
                route = "game_truth"
            ),
            GameModel(
                id = "high_low",
                title = "Mayor o Menor",
                description = "Adivina si la próxima carta es más alta o baja.",
                iconRes = R.drawable.highlow, // <--- Cambia este icono si quieres
                color = SecondaryPink,
                route = "game_highlow"
            ),
            GameModel(
                id = "medusa",
                title = "La Medusa",
                description = "No cruces miradas o bebes.",
                iconRes = R.drawable.medusa, // <--- Cambia este icono si quieres
                color = AccentCyan,
                route = "game_medusa"
            ),
            GameModel(
                id = "charades",
                title = "Mímica Borracha",
                description = "Actúa sin hablar. Tu equipo debe adivinar.",
                iconRes = R.drawable.mimic, //
                color = AccentAmber,
                route = "game_charades"
            ),
            GameModel(
                id = "roulette",
                title = "Ruleta Rusa",
                description = "6 cámaras, 1 disparo. ¿Tendrás suerte?",
                iconRes = R.drawable.ruleta, //
                color = AccentRed,
                route = "game_roulette"
            ),
            GameModel(
                id = "judge",
                title = "El Juez",
                description = "Obedece la regla absurda o bebe.",
                iconRes = R.drawable.juez, // <--- Cambia este icono si quieres
                color = AccentAmber,
                route = "game_judge"
            ),
            GameModel(
                id = "staring",
                title = "Duelo de Miradas",
                description = "El primero en parpadear pierde.",
                iconRes = R.drawable.duelo, // <--- Cambia este icono si quieres
                color = PrimaryViolet,
                route = "game_staring"
            )
        )
    }
}