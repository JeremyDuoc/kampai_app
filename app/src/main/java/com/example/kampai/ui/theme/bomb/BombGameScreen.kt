package com.example.kampai.ui.theme.bomb

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BombGameScreen(
    viewModel: BombViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val category by viewModel.category.collectAsState()

    // Animación simple de pulsación
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (uiState is BombViewModel.GameState.Playing) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón Volver
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is BombViewModel.GameState.Idle -> {
                    Text(
                        text = "LA BOMBA",
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                        color = Color.Red
                    )
                    Text(
                        text = "Pasa el móvil rápido...",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                is BombViewModel.GameState.Playing -> {
                    Text("MENCIONA UN...", color = Color.Gray)
                    Text(
                        text = category,
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(200.dp)
                            .scale(pulseScale)
                            .border(4.dp, Color.Red, CircleShape)
                            .background(Color.Red.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Text(
                            text = "$timeLeft",
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                            color = Color.White
                        )
                    }
                }
                is BombViewModel.GameState.Exploded -> {
                    Text(
                        text = "¡BOOM!",
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                        color = Color.Red,
                        modifier = Modifier.scale(1.5f)
                    )
                    Text(
                        text = "Te toca beber 🍺",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }
        }

        // Botón Acción
        Button(
            onClick = {
                if (uiState is BombViewModel.GameState.Exploded) viewModel.resetGame()
                else if (uiState is BombViewModel.GameState.Idle) viewModel.startGame()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = uiState !is BombViewModel.GameState.Playing
        ) {
            Text(if (uiState is BombViewModel.GameState.Exploded) "Reiniciar" else "Encender Mecha")
        }
    }
}