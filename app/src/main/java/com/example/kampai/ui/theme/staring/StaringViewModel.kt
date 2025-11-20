package com.example.kampai.ui.theme.staring

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
class StaringViewModel @Inject constructor() : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(GameState.Idle)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _count = MutableStateFlow(3)
    val count: StateFlow<Int> = _count.asStateFlow()

    sealed class GameState {
        object Idle : GameState()
        object Counting : GameState()
        object Fight : GameState()
    }

    private var timerJob: Job? = null

    fun startDuel() {
        _gameState.value = GameState.Counting
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 3 downTo 1) {
                _count.value = i
                delay(1000)
            }
            _gameState.value = GameState.Fight
        }
    }

    fun reset() {
        _gameState.value = GameState.Idle
    }
}