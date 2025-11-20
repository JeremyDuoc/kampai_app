package com.example.kampai.ui.theme.never

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NeverViewModel @Inject constructor() : ViewModel() {

    private val defaultQuestions = listOf(
        "Yo nunca he enviado un mensaje a mi ex borracho/a.",
        "Yo nunca he mentido en este juego.",
        "Yo nunca he besado a alguien en esta habitación.",
        "Yo nunca he sido expulsado de un bar.",
        "Yo nunca he usado una aplicación de citas.",
        "Yo nunca he tenido una cita a ciegas desastrosa.",
        "Yo nunca he robado algo (aunque sea pequeño).",
        "Yo nunca he dicho 'te amo' sin sentirlo."
    )

    private val _currentQuestion = MutableStateFlow(defaultQuestions.first())
    val currentQuestion: StateFlow<String> = _currentQuestion.asStateFlow()

    fun nextQuestion() {
        _currentQuestion.value = defaultQuestions.random()
    }
}