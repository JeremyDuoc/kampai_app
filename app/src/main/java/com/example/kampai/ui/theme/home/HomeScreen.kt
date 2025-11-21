// Rediseño completo del HomeScreen.
// Enfoque: Limpio, profesional, con jerarquía clara y énfasis en el color de los iconos.

package com.example.kampai.ui.theme.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.R
import com.example.kampai.domain.models.GameModel
import com.example.kampai.ui.theme.PrimaryViolet
import com.example.kampai.ui.theme.SurfaceDark
import com.example.kampai.ui.theme.TextGray

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onGameSelected: (String) -> Unit
) {
    val games by viewModel.games.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 26.dp)
    ) {
        item { TopBarSection() }
        item { HeroBanner() }
        item { SectionTitle("Juegos disponibles") }
        items(games) { game -> GameCardRedesigned(game, onGameSelected) }
    }
}

// ---------------- TOP BAR (Optimizado) ------------------
@Composable
fun TopBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.logo_kampai),
                contentDescription = "Logo Kampai",
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Kampai!!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                ),
                color = Color.White
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, contentDescription = "Configuracion", tint = Color.White)
            }
        }
    }
}

// ---------------- HERO BANNER (Limpio y legible) ------------------
@Composable
fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        PrimaryViolet.copy(alpha = 0.9f),
                        PrimaryViolet.copy(alpha = 0.6f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(24.dp)
        ) {
            Text(
                text = "¡Elige tu veneno!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 38.sp
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "¡Borrate con tus amigos, o con tu pareja!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f) // Mejor contraste
            )
        }
    }
    Spacer(Modifier.height(26.dp))
}

// ---------------- SECTION TITLE ------------------
@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        color = Color.White
    )
}

@Composable
fun GameCardRedesigned(game: GameModel, onClick: (String) -> Unit) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "scaleAnim"
    )

    val cardColor by animateColorAsState(
        targetValue = if (pressed) game.color.copy(alpha = 0.18f) else SurfaceDark,
        animationSpec = tween(150), // Animación rápida de color
        label = "colorAnim"
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        try { awaitRelease() } finally { pressed = false }
                    },
                    onTap = { onClick(game.route) }
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Borde sutil usando el color del juego para definición
                .border(1.dp, game.color.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(game.color.copy(alpha = 0.15f)), // Fondo que resalta el icono colorido
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(game.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp) // Icono más grande
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Texto principal
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = game.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Flecha
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = game.color
            )
        }
    }
}