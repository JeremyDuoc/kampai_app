package com.example.kampai.ui.theme.charades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharadesViewModel @Inject constructor() : ViewModel() {

    private val words = listOf(
        "Borracho", "T-Rex", "Caminar sobre fuego", "Bailar Salsa",
        "Enhebrar una aguja", "Pelea de gallos", "Surfear",
        "Cambiar un pañal", "Astronauta", "Zombie"
    )

    private val _gameState = MutableStateFlow<GameState>(GameState.Idle)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _timeLeft = MutableStateFlow(60)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    sealed class GameState {
        object Idle : GameState()
        object Playing : GameState()
        object Finished : GameState()
    }

    private var timerJob: Job? = null

    fun startGame() {
        _currentWord.value = words.random()
        _timeLeft.value = 60
        _gameState.value = GameState.Playing

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
            _gameState.value = GameState.Finished
        }
    }

    fun gotIt() {
        timerJob?.cancel()
        _gameState.value = GameState.Finished
    }

    fun skip() {
        _currentWord.value = words.random()
    }

    fun reset() {
        _gameState.value = GameState.Idle
    }
}