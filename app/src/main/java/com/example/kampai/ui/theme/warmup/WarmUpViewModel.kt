package com.example.kampai.ui.theme.warmup

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kampai.domain.models.PlayerModel
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
        data class Phrase(val text: String, val emoji: String, val color: Color) : WarmupAction()
        data class Event(
            val eventType: EventType,
            val title: String,
            val description: String,
            val selectedPlayer: PlayerModel?,
            val emoji: String,
            val color: Color,
            val instruction: String,
            val penaltyDrinks: Int = 2
        ) : WarmupAction()
    }

    enum class EventType {
        CHALLENGE,      // Reto específico
        MEDUSA,         // La Medusa - todos deben participar
        TRUTH_OR_DARE,  // Verdad o Reto - jugador seleccionado elige
        ROULETTE,       // Ruleta Rusa - jugador seleccionado
        SHOT_CHALLENGE, // Reto de shots
        SPEED_TEST,     // Prueba de velocidad
        DANCE_BATTLE,   // Batalla de baile
        MIMIC_DUEL,      // Duelo de mímica
        MOST_LIKELY //QUIEN ES MÁS PROBABLE QUE
    }

    sealed class GameState {
        object Idle : GameState()
        data class ShowingAction(val action: WarmupAction, val number: Int, val total: Int) : GameState()
        data class ShowingEvent(val event: WarmupAction.Event) : GameState()
        object Finished : GameState()
    }

    private val _gameState = MutableStateFlow<GameState>(GameState.Idle)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _selectedPlayerForEvent = MutableStateFlow<PlayerModel?>(null)
    val selectedPlayerForEvent: StateFlow<PlayerModel?> = _selectedPlayerForEvent.asStateFlow()

    private val phrases = listOf(
        Triple("¡TODOS LOS HOMBRES BEBEN!", "🍺", Color(0xFF2563EB)),
        Triple("¡TODAS LAS MUJERES BEBEN!", "🍷", Color(0xFFEC4899)),
        Triple("El último en ponerse de pie: 2 SHOTS", "🏃", Color(0xFFF59E0B)),
        Triple("El último en tocar el suelo: BEBE", "👇", Color(0xFF10B981)),
        Triple("El más joven: DISTRIBUYE 3 TRAGOS", "🎂", Color(0xFF8B5CF6)),
        Triple("El más mayor: CREA UNA REGLA", "👴", Color(0xFF6366F1)),
        Triple("Quien tenga más hermanos: BEBE", "👨‍👩‍👧‍👦", Color(0xFFEF4444)),
        Triple("¡TODOS BEBEN!", "🎉", Color(0xFFDC2626)),
        Triple("El más alto: ELIGE QUIEN BEBE", "📏", Color(0xFF14B8A6)),
        Triple("El de cumpleaños más cercano: 2 SHOTS", "🎈", Color(0xFFF97316)),
        Triple("Quien vino en vehículo: BEBE", "🚗", Color(0xFF06B6D4)),
        Triple("Los solteros: BEBEN", "💔", Color(0xFFDB2777)),
        Triple("Los comprometidos: BEBEN", "💍", Color(0xFFF43F5E)),
        Triple("Último en levantar la mano: 3 SHOTS", "✋", Color(0xFFA855F7)),
        Triple("El que tenga el móvil más viejo: BEBE", "📱", Color(0xFF64748B))
    )

    private val events = listOf(
        // Retos de shots
        EventDefinition(
            type = EventType.SHOT_CHALLENGE,
            title = "RETO DE SHOTS",
            emoji = "🥃",
            color = Color(0xFFEF4444),
            descriptions = listOf(
                "debe beber 2 shots agachado",
                "debe beber 1 shot sin usar las manos",
                "debe beber 2 shots y girar 3 veces",
                "debe beber 3 shots en 10 segundos"
            ),
            instruction = "Completa el reto o bebe 2 tragos extra de penalización"
        ),
        // Retos de velocidad
        EventDefinition(
            type = EventType.SPEED_TEST,
            title = "PRUEBA DE VELOCIDAD",
            emoji = "⚡",
            color = Color(0xFFF59E0B),
            descriptions = listOf(
                "debe nombrar 5 países en 10 segundos",
                "debe decir 10 palabras que rimen con 'ON' en 15 segundos",
                "debe nombrar 7 marcas de cerveza en 12 segundos"
            ),
            instruction = "¡Rápido! Si no lo logras, bebes 2 tragos"
        ),
        // Retos de valentía
        EventDefinition(
            type = EventType.CHALLENGE,
            title = "RETO DE VALENTÍA",
            emoji = "😨",
            color = Color(0xFF8B5CF6),
            descriptions = listOf(
                "debe hacer 15 sentadillas mientras bebe",
                "debe cantar una canción en voz alta sin parar",
                "debe lamer el cuello de la persona a su derecha",
                "debe decir algo vergonzoso que haya hecho"
            ),
            instruction = "¿Te atreves? Si no, bebe 2 tragos"
        ),
        // Verdad o Reto rápido
        EventDefinition(
            type = EventType.TRUTH_OR_DARE,
            title = "VERDAD O RETO RÁPIDO",
            emoji = "🎲",
            color = Color(0xFF06B6D4),
            descriptions = listOf(
                "¿Cuál es tu mayor secreto?",
                "¿A quién aquí le darías un beso?",
                "¿Cuál es tu peor hábito?"
            ),
            instruction = "Elige: ¿Verdad o Reto? Si rechazas, bebes 2 tragos"
        ),
        // Batallas de baile
        EventDefinition(
            type = EventType.DANCE_BATTLE,
            title = "BATALLA DE BAILE",
            emoji = "💃",
            color = Color(0xFFEC4899),
            descriptions = listOf(
                "debe bailar 30 segundos sin parar",
                "debe bailar imitando a alguien del grupo",
                "debe hacer un baile viral de TikTok"
            ),
            instruction = "¡Muéstrate! Si no bailas, bebes 2 tragos"
        ),

        // --- NUEVO: QUIÉN ES MÁS PROBABLE ---
        EventDefinition(
            type = EventType.MOST_LIKELY,
            title = "QUIÉN ES MÁS PROBABLE",
            emoji = "👉",
            color = Color(0xFF0EA5E9),
            descriptions = listOf(
                "que hoy termine vomitando",
                "que se case primero",
                "que acabe en la cárcel algún día",
                "que se vuelva millonario",
                "que llame a su ex esta noche",
                "que se una a una secta"
            ),
            instruction = "A la cuenta de 3, todos señalan a alguien. El más señalado BEBE."
        ),

        // --- NUEVO: LA MEDUSA ---
        EventDefinition(
            type = EventType.MEDUSA,
            title = "LA MEDUSA",
            emoji = "🐍",
            color = Color(0xFF10B981),
            descriptions = listOf("Todos agachan la cabeza..."),
            instruction = "Cuenten hasta 3 y miren a alguien. Si cruzas miradas con esa persona, deben gritar ¡MEDUSA! y AMBOS BEBEN"
        ),

        // --- NUEVO: DUELO DE MÍMICA ---
        EventDefinition(
            type = EventType.MIMIC_DUEL,
            title = "DUELO DE MÍMICA",
            emoji = "🎭",
            color = Color(0xFFFFD700),
            descriptions = listOf(
                "debe imitar a otro jugador sin hablar",
                "debe imitar un animal haciendo el amor",
                "debe imitar su posición sexual favorita"
            ),
            instruction = "Los demás adivinan. Si nadie adivina en 30s, BEBES."
        ),

    )


    private var currentRound = 0
    private val totalRounds = 50 //Cantidad de rondas
    private val eventFrequency = 4 //Frecuencia de eventos en rondas (cada 4 rondas)
    private var eventPlayers: List<PlayerModel> = emptyList()

    fun setPlayers(players: List<PlayerModel>) {
        eventPlayers = players
    }

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
        // Probabilidad de evento basada en rondas
        val shouldShowEvent = currentRound > 0 && currentRound % eventFrequency == 0 && Random.nextBoolean()

        val action = if (shouldShowEvent && eventPlayers.isNotEmpty()) {
            generateRandomEvent()
        } else {
            val (text, emoji, color) = phrases.random()
            WarmupAction.Phrase(text, emoji, color)
        }

        when (action) {
            is WarmupAction.Event -> {
                _selectedPlayerForEvent.value = action.selectedPlayer
                _gameState.value = GameState.ShowingEvent(action)
            }
            else -> {
                _gameState.value = GameState.ShowingAction(action, currentRound + 1, totalRounds)
            }
        }
    }

    private fun generateRandomEvent(): WarmupAction {
        val eventDef = events.random()

        val selectedPlayer = if (eventDef.type == EventType.MOST_LIKELY || eventDef.type == EventType.MEDUSA) {
            null
        } else {
            eventPlayers.random()
        }

        val description = eventDef.descriptions.random()

        return WarmupAction.Event(
            eventType = eventDef.type,
            title = eventDef.title,
            description = description,
            selectedPlayer = selectedPlayer,
            emoji = eventDef.emoji,
            color = eventDef.color,
            instruction = eventDef.instruction,
            penaltyDrinks = 2
        )
    }

    fun acceptChallenge() {
        // El jugador aceptó el reto - se espera que lo complete
        nextAction()
    }

    fun rejectChallenge() {
        // El jugador rechazó - penalización de 2 tragos
        nextAction()
    }

    fun reset() {
        _gameState.value = GameState.Idle
        _selectedPlayerForEvent.value = null
        currentRound = 0
    }

    private data class EventDefinition(
        val type: EventType,
        val title: String,
        val emoji: String,
        val color: Color,
        val descriptions: List<String>,
        val instruction: String
    )
}