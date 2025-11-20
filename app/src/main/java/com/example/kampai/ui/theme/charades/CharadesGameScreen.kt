package com.example.kampai.ui.theme.charades

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.ui.theme.AccentAmber

@Composable
fun CharadesGameScreen(
    viewModel: CharadesViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.gameState.collectAsState()
    val word by viewModel.currentWord.collectAsState()
    val time by viewModel.timeLeft.collectAsState()

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
                text = "Mímica Borracha",
                style = MaterialTheme.typography.titleLarge,
                color = AccentAmber,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is CharadesViewModel.GameState.Idle -> {
                    Text(
                        text = "Actúa sin hablar",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tu equipo tiene 60 segundos.",
                        color = Color.Gray
                    )
                }
                is CharadesViewModel.GameState.Playing -> {
                    Text(
                        text = "$time",
                        style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (time < 10) Color.Red else Color.White
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = word,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                        textAlign = TextAlign.Center,
                        color = AccentAmber
                    )
                }
                is CharadesViewModel.GameState.Finished -> {
                    Text(
                        text = if (time > 0) "¡ADIVINADO!" else "¡TIEMPO!",
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                        color = if (time > 0) Color.Green else Color.Red
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    if (time == 0) {
                        Text("Palabra era: $word", color = Color.Gray)
                        Text("¡Todos beben!", color = Color.White, fontSize = 24.sp)
                    }
                }
            }
        }

        if (state == CharadesViewModel.GameState.Playing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.skip() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f).height(64.dp)
                ) {
                    Text("Pasar (-5s)")
                }
                Button(
                    onClick = { viewModel.gotIt() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    modifier = Modifier.weight(1f).height(64.dp)
                ) {
                    Text("¡Correcto!", color = Color.Black)
                }
            }
        } else {
            Button(
                onClick = {
                    if (state == CharadesViewModel.GameState.Finished) viewModel.reset()
                    else viewModel.startGame()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(
                    text = if (state == CharadesViewModel.GameState.Idle) "Empezar" else "Volver",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}