package com.example.kampai.ui.theme.bomb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kampai.utils.SoundManager
import com.example.kampai.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class BombViewModel @Inject constructor(
    private val soundManager: SoundManager
) : ViewModel() {

    sealed class GameState {
        object Idle : GameState()
        object Playing : GameState()
        object Exploded : GameState()
    }

    private val _uiState = MutableStateFlow<GameState>(GameState.Idle)
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private var timerJob: Job? = null

    private val categories = listOf(
        "Marcas de Coches", "Pokémones", "Capitales de Europa",
        "Marcas de Cerveza", "Ingredientes de Pizza", "Películas Disney",
        "Partes del Cuerpo", "Rima con 'RON'", "Superhéroes Marvel",
        "Cosas que encuentras en un baño", "Razas de Perros"
    )

    fun startGame() {
        val duration = Random.nextInt(10, 30)
        _category.value = categories.random()
        _timeLeft.value = duration
        _uiState.value = GameState.Playing

        timerJob?.cancel()
        timerJob = viewModelScope.launch {

            while (_timeLeft.value > 0) {
                try {
                    soundManager.playSound(R.raw.tic_tac)
                } catch (e: Exception) {
                }

                delay(1000)

                _timeLeft.value -= 1
            }

            // Cuando el tiempo llega a 0, explota
            _uiState.value = GameState.Exploded
            try {
                soundManager.playSound(R.raw.explosion)
            } catch (e: Exception) { }
        }
    }

    fun resetGame() {
        timerJob?.cancel()
        soundManager.stopSound()
        _uiState.value = GameState.Idle
        _timeLeft.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager.stopSound()
    }
}