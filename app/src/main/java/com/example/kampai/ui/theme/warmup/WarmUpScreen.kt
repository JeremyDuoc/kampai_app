package com.example.kampai.ui.theme.warmup

import android.net.Uri
import androidx.annotation.OptIn
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.kampai.R
import com.example.kampai.ui.theme.PrimaryViolet
import com.example.kampai.ui.theme.partymanager.PartyManagerViewModel
import kotlinx.coroutines.delay


@Composable
fun WarmupGameScreen(
    viewModel: WarmupViewModel = hiltViewModel(),
    partyViewModel: PartyManagerViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    val players by partyViewModel.players.collectAsState()

    // Pasar los jugadores al ViewModel
    LaunchedEffect(players) {
        viewModel.setPlayers(players)
    }

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
                            else -> {}
                        }
                    }
                    is WarmupViewModel.GameState.ShowingEvent -> {
                        EventPopup(
                            event = state.event,
                            onAccept = { viewModel.acceptChallenge() },
                            onReject = { viewModel.rejectChallenge() }
                        )
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
fun EventPopup(
    event: WarmupViewModel.WarmupAction.Event,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                event.color.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .border(
                        width = 3.dp,
                        color = event.color,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(32.dp),
                        ambientColor = event.color,
                        spotColor = event.color
                    )
                    .padding(32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Animación de alerta
                    AlertAnimation(emoji = event.emoji)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Título del evento
                    Text(
                        text = "¡UN EVENTO HA APARECIDO!",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Black
                        ),
                        color = event.color
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tipo de evento
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))



                    if (event.selectedPlayer != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(12.dp, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = event.color.copy(alpha = 0.2f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    // Al estar dentro del if, Kotlin sabe que no es null
                                                    event.selectedPlayer.getAvatarColor().copy(alpha = 0.8f),
                                                    event.selectedPlayer.getAvatarColor().copy(alpha = 0.4f)
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = event.selectedPlayer.gender.getEmoji(),
                                        fontSize = 28.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = "Jugador seleccionado:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = event.selectedPlayer.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    // --- FIN DE LA MODIFICACIÓN ---

                    // Descripción del reto
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.08f)
                        )
                    ) {
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                lineHeight = 32.sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Instrucción
                    Text(
                        text = event.instruction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                showDialog = false
                                onReject()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red.copy(alpha = 0.8f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Rechazar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "-2 tragos",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                showDialog = false
                                onAccept()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = event.color
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Aceptar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Reto",
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlertAnimation(emoji: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "alert")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Text(
        text = emoji,
        fontSize = 120.sp,
        modifier = Modifier
            .scale(scale)
            .graphicsLayer { rotationZ = rotation }
    )
}

// Resto de composables (WarmupBackground, WarmupHeader, etc.) permanecen igual
@OptIn(UnstableApi::class)
@Composable
fun WarmupBackground() {
    val context = LocalContext.current

    // Inicializamos el ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Referencia al video en res/raw/background_video
            val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.background_video}")
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 0f
        }
    }

    // Liberar memoria cuando salimos de la pantalla
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false // Ocultar controles de pausa/play
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM // Zoom para llenar pantalla sin bordes negros
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        //Una capa oscura encima para que el texto blanco se lea bien
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
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
                text = "🔥 PartyMix",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                ),
                color = Color(0xFFF59E0B)
            )
            Text(
                text = "Con eventos aleatorios",
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
        Text(text = "🎯", fontSize = 120.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Modo PartyMix",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Diferentes rondas con frases y eventos aleatorios. ¡Un jugador aleatorio será seleccionado para cada reto!",
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
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ronda $currentRound/$totalRounds",
                style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = emoji, fontSize = 100.sp)

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                color.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = color.copy(alpha = 0.5f),
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
        Text(text = "🎉", fontSize = 140.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "¡PartyMix\nCompletado!",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Black
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 48.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Espero que la estén pasando bien!",
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
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(16.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "🔥 Jugar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        is WarmupViewModel.GameState.ShowingAction -> {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryViolet),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Siguiente", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
        is WarmupViewModel.GameState.Finished -> {
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
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
        else -> {}
    }
}

@Composable
fun graphicsLayer(
    block: androidx.compose.ui.graphics.GraphicsLayerScope.() -> Unit
): Modifier = Modifier.graphicsLayer(block = block)