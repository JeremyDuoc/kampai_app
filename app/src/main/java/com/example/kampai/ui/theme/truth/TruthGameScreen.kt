package com.example.kampai.ui.theme.truth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // <- CAMBIO AQUÍ: Usamos el estándar
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
fun TruthGameScreen(
    viewModel: TruthViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                // Usamos Icons.Filled.ArrowBack
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Verdad o Reto",
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
            when (val state = uiState) {
                is TruthViewModel.GameState.Selection -> {
                    Button(
                        onClick = { viewModel.pickTruth() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("VERDAD", fontSize = 24.sp, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("O", color = Color.Gray, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.pickDare() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("RETO", fontSize = 24.sp, fontWeight = FontWeight.Black)
                    }
                }
                is TruthViewModel.GameState.Result -> {
                    val color = if (state.type == TruthViewModel.Type.TRUTH) Color(0xFF60A5FA) else Color(0xFFF87171)
                    Text(
                        text = if (state.type == TruthViewModel.Type.TRUTH) "VERDAD" else "RETO",
                        color = color,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = state.text,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 40.sp
                    )
                }
            }
        }

        if (uiState is TruthViewModel.GameState.Result) {
            Button(
                onClick = { viewModel.reset() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Volver a elegir", color = Color.White)
            }
        }
    }
}