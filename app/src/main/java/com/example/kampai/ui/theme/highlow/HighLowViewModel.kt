package com.example.kampai.ui.theme.highlow

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HighLowViewModel @Inject constructor() : ViewModel() {

    private val _currentCard = MutableStateFlow(generateCard())
    val currentCard: StateFlow<Int> = _currentCard.asStateFlow()

    private val _message = MutableStateFlow("¿La próxima es Mayor o Menor?")
    val message: StateFlow<String> = _message.asStateFlow()

    private fun generateCard(): Int {
        return Random.nextInt(1, 14) // Cartas del 1 al 13
    }

    fun guessHigher() {
        val next = generateCard()
        val current = _currentCard.value

        if (next >= current) {
            _message.value = "¡Correcto! Era $next"
        } else {
            _message.value = "¡Incorrecto! Era $next. ¡BEBE!"
        }
        _currentCard.value = next
    }

    fun guessLower() {
        val next = generateCard()
        val current = _currentCard.value

        if (next <= current) {
            _message.value = "¡Correcto! Era $next"
        } else {
            _message.value = "¡Incorrecto! Era $next. ¡BEBE!"
        }
        _currentCard.value = next
    }
}