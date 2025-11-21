package com.example.kampai.ui.theme.never

import androidx.lifecycle.ViewModel
import com.example.kampai.data.GameContent // Importar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NeverViewModel @Inject constructor() : ViewModel() {

    // Usamos la lista masiva
    private val _currentQuestion = MutableStateFlow(GameContent.neverQuestions.random())
    val currentQuestion = _currentQuestion.asStateFlow()

    fun nextQuestion() {
        _currentQuestion.value = GameContent.neverQuestions.random()
    }
}