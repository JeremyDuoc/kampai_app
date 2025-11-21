package com.example.kampai.ui.theme.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.R
import com.example.kampai.domain.models.GameModel
import com.example.kampai.ui.theme.TextGray

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onGameSelected: (String) -> Unit
) {
    val games by viewModel.games.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(24.dp))
        GamesList(games, onGameSelected)
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo con animación de entrada
        val scale = remember { Animatable(0.8f) }

        LaunchedEffect(Unit) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        Image(
            painter = painterResource(id = R.drawable.logo_kampai),
            contentDescription = "Logo Kampai",
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(top = 16.dp)
                .scale(scale.value),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Que empiece la fiesta 🎉",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun GamesList(games: List<GameModel>, onGameSelected: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        items(games) { game ->
            GameCard(game, onGameSelected)
        }
    }
}

@Composable
fun GameCard(game: GameModel, onClick: (String) -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        game.color.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = game.color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick(game.route)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono con efecto glow
        Box(
            modifier = Modifier
                .size(70.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = game.color,
                    spotColor = game.color
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            game.color.copy(alpha = 0.3f),
                            game.color.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = game.iconRes), // <--- USAR ESTO PARA PNG
                contentDescription = null,
                // Importante: Si tu PNG tiene colores propios y quieres que se vean, usa Color.Unspecified.
                tint = Color.Unspecified,
                modifier = Modifier.size(68.dp) // Aumenta un poco el tamaño si el PNG se ve chico
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
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

        Spacer(modifier = Modifier.width(12.dp))

        // Flecha con gradiente
        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = null,
            tint = game.color,
            modifier = Modifier.size(24.dp)
        )
    }
}