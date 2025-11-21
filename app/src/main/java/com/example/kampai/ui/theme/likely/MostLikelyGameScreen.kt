package com.example.kampai.ui.theme.likely

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.ui.theme.AccentAmber
import com.example.kampai.ui.theme.GameScaffold
import com.example.kampai.ui.theme.KampaiCard

@Composable
fun MostLikelyScreen(
    viewModel: MostLikelyViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val question by viewModel.question.collectAsState()

    GameScaffold(title = "¿Quién es más probable?", color = AccentAmber, onBack = onBack) {

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "A la cuenta de 3, señalen a una persona...",
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        KampaiCard(modifier = Modifier.fillMaxWidth().weight(1f), borderColor = AccentAmber) {
            Text(
                text = question,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.nextQuestion() },
            colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Siguiente Pregunta", fontSize = 18.sp, color = Color.Black)
        }
    }
}