package com.albin.betdaylite.presentation.betdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.albin.betdaylite.domain.model.BetStatus
import java.util.Locale

@Composable
fun BetDetailScreen(
    viewModel: BetDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when (val uiState = state) {
        BetDetailUiState.Loading -> LoadingContent()
        BetDetailUiState.NotFound -> MessageContent("No encontramos la apuesta seleccionada")
        is BetDetailUiState.Error -> MessageContent(uiState.message)
        is BetDetailUiState.Success -> BetDetailContent(detail = uiState.detail)
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Cargando detalle...")
    }
}

@Composable
private fun MessageContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun BetDetailContent(detail: BetDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = detail.teams,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = detail.leagueName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Fecha apuesta: ${detail.placedAtText}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DetailLine(label = "Seleccion", value = detail.pickLabel)
                DetailLine(label = "Cuota", value = String.format(Locale.US, "%.2f", detail.odd))
                DetailLine(label = "Apuesta", value = String.format(Locale.US, "$%.2f", detail.stake))
                DetailLine(label = "Retorno potencial", value = String.format(Locale.US, "$%.2f", detail.potentialReturn))
                DetailLine(label = "Estado", value = detail.status.name)
            }
        }

        StatusLegend(status = detail.status)
        Text(
            text = "ID: ${detail.betId}",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun DetailLine(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun StatusLegend(status: BetStatus) {
    val (containerColor, textColor, message) = when (status) {
        BetStatus.PENDING -> Triple(Color(0xFFFFF8E1), Color(0xFFF9A825), "Pendiente: el partido aun no termina")
        BetStatus.WON -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), "Ganada: el resultado coincide con tu seleccion")
        BetStatus.LOST -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Perdida: el resultado final fue distinto")
    }

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            color = textColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}



