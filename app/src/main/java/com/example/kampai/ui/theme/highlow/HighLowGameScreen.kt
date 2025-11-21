package com.example.kampai.ui.theme.highlow

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.ui.theme.SecondaryPink
import kotlinx.coroutines.delay

@Composable
fun HighLowGameScreen(
    viewModel: HighLowViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val currentCard by viewModel.currentCard.collectAsState()
    val nextCard by viewModel.nextCard.collectAsState()
    val message by viewModel.message.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val gameActive by viewModel.gameActive.collectAsState()
    val lastResult by viewModel.lastResult.collectAsState()
    // 1. NUEVO: Recolectar el estado de la relación de la carta para la flecha
    val cardRelation by viewModel.cardRelation.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HighLowBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HighLowHeader(onBack = onBack, streak = streak)

            Spacer(modifier = Modifier.height(32.dp))

            // Instrucción
            InstructionBadge(text = "¿La próxima es Mayor o Menor?")

            Spacer(modifier = Modifier.height(40.dp))

            // Cartas animadas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Carta Actual
                    AnimatedPlayingCard(
                        card = currentCard,
                        isRevealed = true,
                        label = "CARTA ACTUAL"
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Flecha animada
                    AnimatedArrow(
                        // 2. CORRECCIÓN: Usar el nuevo StateFlow CardRelation
                        direction = when (cardRelation) {
                            HighLowViewModel.CardRelation.HIGHER -> "↑"
                            HighLowViewModel.CardRelation.LOWER -> "↓"
                            else -> "→"
                        },
                        isVisible = cardRelation != null // Visible solo si hay una relación calculada
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Carta Siguiente (oculta hasta que se predice)
                    if (gameActive) {
                        AnimatedPlayingCard(
                            card = 0, // Carta oculta
                            isRevealed = false,
                            label = "PRÓXIMA CARTA"
                        )
                    } else {
                        AnimatedPlayingCard(
                            card = nextCard,
                            isRevealed = true,
                            label = "RESULTADO",
                            resultColor = when (lastResult) {
                                HighLowViewModel.Result.CORRECT -> Color.Green
                                HighLowViewModel.Result.WRONG -> Color.Red
                                else -> SecondaryPink
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de resultado
            if (message.isNotEmpty()) {
                ResultMessage(message = message, lastResult = lastResult)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de predicción
            if (gameActive) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PredictionButton(
                        label = "MENOR 👇",
                        color = Color(0xFFEF4444),
                        onClick = { viewModel.guessLower() },
                        modifier = Modifier.weight(1f)
                    )

                    PredictionButton(
                        label = "MAYOR 👆",
                        color = Color(0xFF10B981),
                        onClick = { viewModel.guessHigher() },
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.nextRound() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryPink
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Siguiente Ronda",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedPlayingCard(
    card: Int,
    isRevealed: Boolean,
    label: String,
    resultColor: Color = Color.White
) {
    var showCard by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (showCard) 1f else 0.8f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )

    // 3. CORRECCIÓN: Cambiar el nombre a cardRotation para evitar conflicto de alcance
    val cardRotation by animateFloatAsState(
        targetValue = if (showCard && isRevealed) 0f else 180f,
        animationSpec = tween(600),
        label = "cardRotation"
    )

    LaunchedEffect(card) {
        showCard = false
        delay(100)
        showCard = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                letterSpacing = 2.sp
            ),
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .width(140.dp)
                .height(200.dp)
                .shadow(16.dp, RoundedCornerShape(16.dp))
                // 4. CORRECCIÓN: Usar el nuevo nombre de la variable de animación
                .graphicsLayer { rotationY = cardRotation },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isRevealed) Color.White else MaterialTheme.colorScheme.surface
            )
        ) {
            if (isRevealed) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val (cardText, cardColor) = getCardDisplay(card)

                        Text(
                            text = cardText,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = cardColor
                        )

                        Text(
                            text = cardText,
                            fontSize = 80.sp,
                            fontWeight = FontWeight.Black,
                            color = cardColor,
                            modifier = Modifier.offset(y = 8.dp)
                        )

                        Text(
                            text = cardText,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = cardColor,
                            modifier = Modifier.rotate(180f)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF7C3AED),
                                    Color(0xFF6366F1)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("?", fontSize = 60.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("?", fontSize = 60.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun PredictionButton(
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = modifier
            .height(64.dp)
            .scale(scale)
            .shadow(12.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        interactionSource = interactionSource
    ) {
        Text(label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ResultMessage(
    message: String,
    lastResult: HighLowViewModel.Result?
) {
    val color = when (lastResult) {
        HighLowViewModel.Result.CORRECT -> Color.Green
        HighLowViewModel.Result.WRONG -> Color.Red
        else -> Color.Gray
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun HighLowBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-100).dp, y = (-100).dp)
                .size(300.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF10B981).copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .size(350.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEF4444).copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun HighLowHeader(onBack: () -> Unit, streak: Int) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎴 Mayor o Menor",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = SecondaryPink
            )
            Text(
                text = "Racha: $streak 🔥",
                style = MaterialTheme.typography.labelMedium,
                color = if (streak > 0) Color(0xFFF59E0B) else Color.Gray
            )
        }

        if (streak > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF59E0B).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("$streak", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
            }
        }
    }
}

@Composable
fun InstructionBadge(text: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badgeScale"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        modifier = Modifier.scale(scale)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun AnimatedArrow(direction: String, isVisible: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "arrow")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (direction == "↑") -10f else if (direction == "↓") 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrowOffset"
    )

    if (isVisible) {
        Text(
            text = direction,
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = when (direction) {
                "↑" -> Color(0xFF10B981)
                "↓" -> Color(0xFFEF4444)
                else -> SecondaryPink
            },
            modifier = Modifier.offset(y = offsetY.dp)
        )
    }
}

private fun getCardDisplay(value: Int): Pair<String, Color> {
    return when (value) {
        1 -> "A" to Color.Black
        11 -> "J" to Color.Red
        12 -> "Q" to Color.Black
        13 -> "K" to Color.Red
        else -> "$value" to if (value % 2 == 0) Color.Black else Color.Red
    }
}