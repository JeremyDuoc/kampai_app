package com.example.kampai.ui.theme.culture

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kampai.ui.theme.PrimaryViolet
import com.example.kampai.ui.theme.AccentRed

@Composable
fun CultureSelectionScreen(
    onNavigateToBomb: () -> Unit,
    onNavigateToClassic: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header ---
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
            Text(
                text = "Elige el Modo",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryViolet,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- Opción 1: Modo Clásico ---
        SelectionCard(
            title = "Modo Clásico",
            description = "Sin tiempo límite. Ideal para charlar y beber tranquilo.",
            color = PrimaryViolet,
            onClick = onNavigateToClassic
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Opción 2: Modo Bomba ---
        SelectionCard(
            title = "Modo Bomba",
            description = "¡Tic-Tac! Responde rápido antes de que explote.",
            color = AccentRed, // Usamos rojo para representar peligro/bomba
            onClick = onNavigateToBomb
        )
    }
}

@Composable
fun SelectionCard(
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Para usar gradiente manual
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.2f),
                            color.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
            }
        }
    }
}