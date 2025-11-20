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
import androidx.compose.ui.graphics.Brush
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

    // Animación de pulsación que se acelera con el tiempo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (uiState is BombViewModel.GameState.Playing) {
            if (timeLeft <= 5) 1.15f else 1.08f
        } else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (timeLeft <= 5) 300 else 500
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = if (uiState is BombViewModel.GameState.Exploded) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF6B6B),
                            Color(0xFFEE5A6F),
                            Color(0xFFC06C84)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón Volver
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "LA BOMBA",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Red,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (uiState) {
                    is BombViewModel.GameState.Idle -> {
                        Text(
                            text = "💣",
                            fontSize = 120.sp,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Text(
                            text = "LA BOMBA",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color.Red
                        )
                        Text(
                            text = "Pasa el móvil rápido antes de que explote",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp)
                        )
                    }
                    is BombViewModel.GameState.Playing -> {
                        Text(
                            "MENCIONA UN...",
                            color = Color.Gray,
                            letterSpacing = 2.sp,
                            fontSize = 14.sp
                        )
                        Text(
                            text = category,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp)
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(220.dp)
                                .scale(pulseScale)
                                .border(
                                    width = 6.dp,
                                    color = if (timeLeft <= 5) Color.Red else Color(0xFFFF6B6B),
                                    shape = CircleShape
                                )
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.Red.copy(alpha = 0.3f),
                                            Color.Red.copy(alpha = 0.1f)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        ) {
                            Text(
                                text = "$timeLeft",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 80.sp
                                ),
                                color = Color.White
                            )
                        }

                        if (timeLeft <= 5) {
                            Text(
                                text = "¡RÁPIDO!",
                                color = Color.Red,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                        }
                    }
                    is BombViewModel.GameState.Exploded -> {
                        Text(
                            text = "💥",
                            fontSize = 150.sp,
                            modifier = Modifier
                                .scale(1.5f)
                                .padding(bottom = 24.dp)
                        )
                        Text(
                            text = "¡BOOM!",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "¡Te toca beber! 🍺",
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    disabledContainerColor = Color.Red.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = uiState !is BombViewModel.GameState.Playing,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (uiState is BombViewModel.GameState.Exploded) "Reiniciar" else "Encender Mecha",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}