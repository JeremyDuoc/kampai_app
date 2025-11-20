package com.example.kampai.data

import androidx.compose.ui.graphics.Color
import com.example.kampai.domain.models.GameModel
import com.example.kampai.ui.theme.AccentAmber
import com.example.kampai.ui.theme.AccentCyan
import com.example.kampai.ui.theme.AccentRed
import com.example.kampai.R
import javax.inject.Inject

class GameRepository @Inject constructor() {
    fun getGames(): List<GameModel> {
        return listOf(
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
                id = "time_bomb",
                title = "La Bomba",
                description = "Pasa el celular antes de que explote en tus manos.",
                iconRes = R.drawable.ic_bomb,
                color = AccentRed,
                route = "game_bomb"
            )
        )
    }
}