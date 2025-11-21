package com.example.kampai.ui.theme.highlow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.ui.theme.GameScaffold
import com.example.kampai.ui.theme.SecondaryPink

@Composable
fun HighLowGameScreen(
    viewModel: HighLowViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val currentCard by viewModel.currentCard.collectAsState()
    val message by viewModel.message.collectAsState()

    // Función auxiliar para pintar la carta
    fun getCardDisplay(value: Int): Pair<String, Color> {
        return when(value) {
            1 -> "A" to Color.Black
            11 -> "J" to Color.Red
            12 -> "Q" to Color.Black
            13 -> "K" to Color.Red
            else -> "$value" to if (value % 2 == 0) Color.Black else Color.Red
        }
    }

    val (cardText, cardColor) = getCardDisplay(currentCard)

    GameScaffold(title = "Mayor o Menor", color = SecondaryPink, onBack = onBack) {

        Spacer(modifier = Modifier.height(20.dp))

        // Carta de Poker Estilizada
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .width(220.dp)
                .height(320.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Esquina superior
                Text(text = cardText, color = cardColor, fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopStart))
                // Centro grande
                Text(text = cardText, color = cardColor, fontSize = 100.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Center))
                // Esquina inferior (invertida simulada)
                Text(text = cardText, color = cardColor, fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomEnd))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { viewModel.guessLower() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                modifier = Modifier.weight(1f).height(64.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("MENOR 👇", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.guessHigher() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                modifier = Modifier.weight(1f).height(64.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("MAYOR 👆", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}