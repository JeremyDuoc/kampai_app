package com.example.kampai.ui.theme.staring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.ui.theme.PrimaryViolet

@Composable
fun StaringGameScreen(
    viewModel: StaringViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.gameState.collectAsState()
    val count by viewModel.count.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text(
                text = "Duelo de Miradas",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryViolet,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is StaringViewModel.GameState.Idle -> {
                    Text("Elige un oponente", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("El primero en parpadear pierde.", color = Color.Gray)
                }
                is StaringViewModel.GameState.Counting -> {
                    Text("$count", fontSize = 120.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                is StaringViewModel.GameState.Fight -> {
                    Text("¡YA!", fontSize = 80.sp, fontWeight = FontWeight.Black, color = PrimaryViolet)
                }
            }
        }

        Button(
            onClick = { if (state == StaringViewModel.GameState.Fight) viewModel.reset() else viewModel.startDuel() },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryViolet),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = state != StaringViewModel.GameState.Counting
        ) {
            Text(if (state == StaringViewModel.GameState.Fight) "Reiniciar" else "¡Duelo!", fontSize = 18.sp)
        }
    }
}