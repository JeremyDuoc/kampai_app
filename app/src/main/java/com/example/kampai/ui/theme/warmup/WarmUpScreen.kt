package com.example.kampai.ui.theme.warmup

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.ui.theme.PrimaryViolet
import com.example.kampai.ui.theme.SecondaryPink
import kotlinx.coroutines.delay

@Composable
fun WarmupGameScreen(
    viewModel: WarmupViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToMiniGame: (String) -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fondo animado
        WarmupBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            WarmupHeader(onBack = onBack)

            Spacer(modifier = Modifier.height(40.dp))

            // Contenido principal
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (val state = gameState) {
                    is WarmupViewModel.GameState.Idle -> {
                        IdleContent()
                    }
                    is WarmupViewModel.GameState.ShowingAction -> {
                        when (val action = state.action) {
                            is WarmupViewModel.WarmupAction.Phrase -> {
                                PhraseContent(
                                    phrase = action.text,
                                    emoji = action.emoji,
                                    color = action.color,
                                    currentRound = state.number,
                                    totalRounds = state.total
                                )
                            }
                            is WarmupViewModel.WarmupAction.MiniGame -> {
                                MiniGameContent(
                                    gameType = action.gameType,
                                    onNavigate = onNavigateToMiniGame,
                                    onSkip = { viewModel.nextAction() }
                                )
                            }
                        }
                    }
                    is WarmupViewModel.GameState.Finished -> {
                        FinishedContent()
                    }
                }
            }

            // Botones de acción
            ActionButtons(
                gameState = gameState,
                onStart = { viewModel.startWarmup() },
                onNext = { viewModel.nextAction() },
                onReset = { viewModel.reset() }
            )
        }
    }
}

@Composable
fun WarmupBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )

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
                            PrimaryViolet.copy(alpha = 0.3f),
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
                            SecondaryPink.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun WarmupHeader(onBack: () -> Unit) {
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
                text = "🔥 Calentamiento",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                ),
                color = Color(0xFFF59E0B)
            )
            Text(
                text = "Prepárense para beber",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun IdleContent() {
    var isVisible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Text(
            text = "🎯",
            fontSize = 120.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Modo Calentamiento",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "12 rondas de puro caos. Frases aleatorias, retos y minijuegos. ¡Que empiece la fiesta!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
fun PhraseContent(
    phrase: String,
    emoji: String,
    color: Color,
    currentRound: Int,
    totalRounds: Int
) {
    var isVisible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (isVisible) color else Color.Transparent,
        animationSpec = tween(600),
        label = "color"
    )

    // Animación de pulso para el emoji
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val emojiScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emojiScale"
    )

    LaunchedEffect(phrase) {
        isVisible = false
        delay(100)
        isVisible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        // Indicador de progreso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ronda $currentRound/$totalRounds",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 1.sp
                ),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Emoji animado
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(emojiScale)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            animatedColor.copy(alpha = 0.3f),
                            animatedColor.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 72.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Frase principal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                animatedColor.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = animatedColor.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = phrase,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        lineHeight = 40.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun MiniGameContent(
    gameType: WarmupViewModel.MiniGameType,
    onNavigate: (String) -> Unit,
    onSkip: () -> Unit
) {
    val (title, emoji, description, route) = when (gameType) {
        WarmupViewModel.MiniGameType.YO_NUNCA -> {
            Quadruple("Yo Nunca", "🙈", "Ronda rápida de Yo Nunca", "game_never")
        }
        WarmupViewModel.MiniGameType.VERDAD_O_RETO -> {
            Quadruple("Verdad o Reto", "🎲", "Alguien debe elegir", "game_truth")
        }
        WarmupViewModel.MiniGameType.QUIEN_ES_MAS_PROBABLE -> {
            Quadruple("¿Quién es más probable?", "👥", "Señalen a alguien", "game_likely")
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "¡MINIJUEGO!",
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            ),
            color = SecondaryPink
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = emoji,
            fontSize = 100.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onSkip,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Saltar", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onNavigate(route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryPink
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("¡Jugar!", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FinishedContent() {
    var isVisible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Text(
            text = "🎉",
            fontSize = 140.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "¡Calentamiento\nCompletado!",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Black
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 48.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ahora sí... ¡La fiesta real comienza!",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButtons(
    gameState: WarmupViewModel.GameState,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onReset: () -> Unit
) {
    when (gameState) {
        is WarmupViewModel.GameState.Idle -> {
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF59E0B)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(16.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "🔥 Empezar Calentamiento",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        is WarmupViewModel.GameState.ShowingAction -> {
            if (gameState.action !is WarmupViewModel.WarmupAction.MiniGame) {
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryViolet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Siguiente",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        is WarmupViewModel.GameState.Finished -> {
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Volver al Inicio",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

// Helper data class para MiniGameContent
private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)