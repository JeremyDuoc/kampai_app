package com.example.kampai.ui.theme.truth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TruthViewModel @Inject constructor() : ViewModel() {

    sealed class GameState {
        object Selection : GameState()
        data class Result(val type: Type, val text: String) : GameState()
    }

    enum class Type { TRUTH, DARE }

    private val _uiState = MutableStateFlow<GameState>(GameState.Selection)
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val truths = listOf(
        "¿Cuál es tu peor hábito?",
        "¿Quién te cae mal de esta habitación?",
        "¿Cuál es tu mayor miedo irracional?",
        "¿Qué es lo más vergonzoso que has buscado en Google?",
        "¿Te arrepientes de algún beso?"
    )

    private val dares = listOf(
        "Haz 10 sentadillas mientras bebes.",
        "Deja que el grupo envíe un mensaje a quien quieran desde tu móvil.",
        "Imita a alguien del grupo hasta que adivinen quién es.",
        "Bebe un trago sin usar las manos.",
        "Habla con acento extranjero las próximas 3 rondas."
    )

    fun pickTruth() {
        _uiState.value = GameState.Result(Type.TRUTH, truths.random())
    }

    fun pickDare() {
        _uiState.value = GameState.Result(Type.DARE, dares.random())
    }

    fun reset() {
        _uiState.value = GameState.Selection
    }
}