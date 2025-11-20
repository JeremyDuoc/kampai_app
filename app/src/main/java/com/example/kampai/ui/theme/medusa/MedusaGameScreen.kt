package com.example.kampai.ui.theme.medusa

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.kampai.ui.theme.AccentCyan

@Composable
fun MedusaGameScreen(
    viewModel: MedusaViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.gameState.collectAsState()
    val count by viewModel.countdown.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
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
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text(
                text = "La Medusa",
                style = MaterialTheme.typography.titleLarge,
                color = AccentCyan,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is MedusaViewModel.GameState.Instructions -> {
                    Text(
                        text = "1. Todos miren abajo\n2. A la cuenta de 3, miren a alguien\n3. Si cruzan miradas...",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "¡MEDUSA!",
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                        color = AccentCyan
                    )
                }
                is MedusaViewModel.GameState.Counting -> {
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black),
                        color = Color.White,
                        modifier = Modifier.scale(scale)
                    )
                    Text(
                        text = "Levanten la vista...",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
                is MedusaViewModel.GameState.Action -> {
                    Text(
                        text = "¡MIRA AHORA!",
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                        color = AccentCyan,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "¿Cruzaste miradas?\n¡BEBE!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Button(
            onClick = {
                if (state is MedusaViewModel.GameState.Action) viewModel.reset()
                else viewModel.startRound()
            },
            colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = state !is MedusaViewModel.GameState.Counting
        ) {
            Text(
                text = if (state is MedusaViewModel.GameState.Action) "Reiniciar" else "Iniciar Cuenta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}