package com.example.kampai.ui.theme.medusa

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
class MedusaViewModel @Inject constructor() : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(GameState.Instructions)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _countdown = MutableStateFlow(3)
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    sealed class GameState {
        object Instructions : GameState()
        object Counting : GameState()
        object Action : GameState()
    }

    private var timerJob: Job? = null

    fun startRound() {
        _gameState.value = GameState.Counting
        _countdown.value = 3

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 3 downTo 1) {
                _countdown.value = i
                delay(1000)
            }
            _gameState.value = GameState.Action
        }
    }

    fun reset() {
        timerJob?.cancel()
        _gameState.value = GameState.Instructions
    }
}