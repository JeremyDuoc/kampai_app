package com.example.kampai.ui.theme.warmup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class WarmupViewModel @Inject constructor() : ViewModel() {

    sealed class WarmupAction {
        data class Phrase(val text: String, val emoji: String, val color: androidx.compose.ui.graphics.Color) : WarmupAction()
        data class MiniGame(val gameType: MiniGameType) : WarmupAction()
    }

    enum class MiniGameType {
        YO_NUNCA,
        VERDAD_O_RETO,
        QUIEN_ES_MAS_PROBABLE
    }

    sealed class GameState {
        object Idle : GameState()
        data class ShowingAction(val action: WarmupAction, val number: Int, val total: Int) : GameState()
        object Finished : GameState()
    }

    private val _gameState = MutableStateFlow<GameState>(GameState.Idle)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val phrases = listOf(
        Triple("¡TODOS LOS HOMBRES BEBEN!", "🍺", androidx.compose.ui.graphics.Color(0xFF2563EB)),
        Triple("¡TODAS LAS MUJERES BEBEN!", "🍷", androidx.compose.ui.graphics.Color(0xFFEC4899)),
        Triple("El último en ponerse de pie: 2 SHOTS", "🏃", androidx.compose.ui.graphics.Color(0xFFF59E0B)),
        Triple("El último en tocar el suelo: BEBE", "👇", androidx.compose.ui.graphics.Color(0xFF10B981)),
        Triple("El más joven: DISTRIBUYE 3 TRAGOS", "🎂", androidx.compose.ui.graphics.Color(0xFF8B5CF6)),
        Triple("El más mayor: CREA UNA REGLA", "👴", androidx.compose.ui.graphics.Color(0xFF6366F1)),
        Triple("Quien tenga más hermanos: BEBE", "👨‍👩‍👧‍👦", androidx.compose.ui.graphics.Color(0xFFEF4444)),
        Triple("¡TODOS BEBEN!", "🎉", androidx.compose.ui.graphics.Color(0xFFDC2626)),
        Triple("El más alto: ELIGE QUIEN BEBE", "📏", androidx.compose.ui.graphics.Color(0xFF14B8A6)),
        Triple("El de cumpleaños más cercano: 2 SHOTS", "🎈", androidx.compose.ui.graphics.Color(0xFFF97316)),
        Triple("Quien vino en vehículo: BEBE", "🚗", androidx.compose.ui.graphics.Color(0xFF06B6D4)),
        Triple("Los solteros: BEBEN", "💔", androidx.compose.ui.graphics.Color(0xFFDB2777)),
        Triple("Los comprometidos: BEBEN", "💍", androidx.compose.ui.graphics.Color(0xFFF43F5E)),
        Triple("Último en levantar la mano: 3 SHOTS", "✋", androidx.compose.ui.graphics.Color(0xFFA855F7)),
        Triple("El que tenga el móvil más viejo: BEBE", "📱", androidx.compose.ui.graphics.Color(0xFF64748B))
    )

    private var currentRound = 0
    private val totalRounds = 12
    private val miniGameFrequency = 4 // Cada 4 frases aparece un minijuego

    fun startWarmup() {
        currentRound = 0
        showNextAction()
    }

    fun nextAction() {
        currentRound++
        if (currentRound >= totalRounds) {
            _gameState.value = GameState.Finished
        } else {
            showNextAction()
        }
    }

    private fun showNextAction() {
        val action = if (currentRound > 0 && currentRound % miniGameFrequency == 0 && Random.nextBoolean()) {
            // Insertar minijuego cada 4 rondas (con 50% probabilidad)
            WarmupAction.MiniGame(MiniGameType.values().random())
        } else {
            // Mostrar frase aleatoria
            val (text, emoji, color) = phrases.random()
            WarmupAction.Phrase(text, emoji, color)
        }

        _gameState.value = GameState.ShowingAction(action, currentRound + 1, totalRounds)
    }

    fun reset() {
        _gameState.value = GameState.Idle
        currentRound = 0
    }
}