package com.example.kampai.ui.theme.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kampai.R
import com.example.kampai.domain.models.GameModel

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
        Spacer(modifier = Modifier.height(16.dp))
        GamesList(games, onGameSelected)
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally // Centramos el contenido
    ) {
        // --- LOGO DE LA APP ---
        // Asegúrate de que tu archivo en 'res/drawable' se llame 'logo_kampai'
        // Si se llama diferente, cambia 'R.drawable.logo_kampai' por tu nombre.
        Image(
            painter = painterResource(id = R.drawable.logo_kampai),
            contentDescription = "Logo Kampai",
            modifier = Modifier
                .height(120.dp) // Ajusta la altura según necesites
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentScale = ContentScale.Fit // Ajusta la imagen sin deformarla
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Que empiece la fiesta",
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick(game.route) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(game.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            // Usamos vectorResource para cargar los XML vectoriales correctamente
            Icon(
                imageVector = ImageVector.vectorResource(id = game.iconRes),
                contentDescription = null,
                tint = game.color,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = androidx.compose.ui.graphics.Color.White
            )
            Text(
                text = game.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                maxLines = 2
            )
        }
    }
}