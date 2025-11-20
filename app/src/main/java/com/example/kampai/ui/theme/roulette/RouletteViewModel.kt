package com.example.kampai.ui.theme.roulette

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RouletteViewModel @Inject constructor() : ViewModel() {

    // Representa el estado de las 6 cámaras: true = disparada, false = segura, null = sin abrir
    private val _chambers = MutableStateFlow<List<Boolean?>>(List(6) { null })
    val chambers: StateFlow<List<Boolean?>> = _chambers.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver.asStateFlow()

    private val _message = MutableStateFlow("Toca una cámara...")
    val message: StateFlow<String> = _message.asStateFlow()

    private var bulletIndex = -1

    init {
        resetGame()
    }

    fun triggerChamber(index: Int) {
        if (_gameOver.value || _chambers.value[index] != null) return

        val currentList = _chambers.value.toMutableList()

        if (index == bulletIndex) {
            // ¡Bang!
            currentList[index] = true // true significa explosión
            _message.value = "¡BANG! Te toca beber 🥃"
            _gameOver.value = true
        } else {
            // Click (Seguro)
            currentList[index] = false // false significa seguro
            _message.value = "Salvado... siguiente."
        }
        _chambers.value = currentList
    }

    fun resetGame() {
        bulletIndex = (0..5).random()
        _chambers.value = List(6) { null }
        _gameOver.value = false
        _message.value = "Toca una cámara..."
    }
}