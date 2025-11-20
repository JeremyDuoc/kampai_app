package com.example.kampai.ui.theme.roulette

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.ui.theme.AccentRed

@Composable
fun RouletteGameScreen(
    viewModel: RouletteViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val chambers by viewModel.chambers.collectAsState()
    val message by viewModel.message.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()

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
                text = "Ruleta Rusa",
                style = MaterialTheme.typography.titleLarge,
                color = AccentRed,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            color = if (gameOver) AccentRed else Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(6) { index ->
                val state = chambers[index]
                val color = when (state) {
                    null -> Color.DarkGray // Sin abrir
                    true -> AccentRed    // Bang!
                    false -> Color.Green // Seguro
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(color)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable(enabled = state == null && !gameOver) {
                            viewModel.triggerChamber(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state == true) "💥" else if (state == false) "😅" else "${index + 1}",
                        fontSize = 32.sp,
                        color = Color.White
                    )
                }
            }
        }

        Button(
            onClick = { viewModel.resetGame() },
            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Girar Tambor (Reiniciar)", fontSize = 18.sp)
        }
    }
}