package com.albin.betdaylite.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.albin.betdaylite.core.constants.BetConstants
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.CardShape
import com.albin.betdaylite.core.designsystem.GoldPrimary
import com.albin.betdaylite.core.designsystem.NeonGreen
import com.albin.betdaylite.core.designsystem.ObsidianBackground
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurface
import com.albin.betdaylite.core.designsystem.ObsidianSurfaceVariant
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.core.designsystem.TextTertiary

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.depositSuccessMessage) {
        uiState.depositSuccessMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearDepositMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ObsidianBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ObsidianBackground)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GoldPrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = AppSpacing.md,
                        end = AppSpacing.md,
                        top = AppSpacing.md,
                        bottom = AppSpacing.xxl
                    ),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                ) {
                    // Tarjeta de Usuario VIP
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CardShape)
                                .border(1.dp, ObsidianBorder, CardShape),
                            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                            shape = CardShape
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.md),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(ObsidianSurfaceVariant)
                                        .border(2.dp, GoldPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(AppSpacing.md))

                                Column {
                                    Text(
                                        text = "Albin",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .clip(PillShape)
                                                .background(GoldPrimary.copy(alpha = 0.15f))
                                                .padding(horizontal = AppSpacing.sm, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "VIP ORO",
                                                color = GoldPrimary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "ID: #895B-2026",
                                            fontSize = 12.sp,
                                            color = TextTertiary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Dashboard Financiero
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CardShape)
                                .border(1.dp, ObsidianBorder, CardShape),
                            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                            shape = CardShape
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.md)
                            ) {
                                Text(
                                    text = "BILLETERA & RENDIMIENTO",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary,
                                    letterSpacing = 1.sp
                                )

                                Spacer(modifier = Modifier.height(AppSpacing.xs))

                                Text(
                                    text = BetConstants.formatCurrency(uiState.wallet?.balance ?: 0.0),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Saldo total disponible para apuestas",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(AppSpacing.md))
                                Divider(color = ObsidianBorder)
                                Spacer(modifier = Modifier.height(AppSpacing.md))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StatItem(
                                        title = "Total Apostado",
                                        value = BetConstants.formatCurrency(uiState.wallet?.totalWagered ?: 0.0),
                                        valueColor = TextPrimary
                                    )
                                    StatItem(
                                        title = "Total Ganado",
                                        value = BetConstants.formatCurrency(uiState.wallet?.totalWon ?: 0.0),
                                        valueColor = NeonGreen
                                    )
                                    StatItem(
                                        title = "Tasa de Acierto",
                                        value = String.format("%.0f%%", uiState.winRate),
                                        valueColor = GoldPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Módulo de Recarga Simulada
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CardShape)
                                .border(1.dp, ObsidianBorder, CardShape),
                            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                            shape = CardShape
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.md)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "RECARGAR FONDOS (MODO DEMO)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(AppSpacing.sm))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                                ) {
                                    val amounts = listOf(100.0, 500.0, 1000.0)
                                    amounts.forEach { amount ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(PillShape)
                                                .background(ObsidianSurfaceVariant)
                                                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), PillShape)
                                                .clickable { viewModel.depositFunds(amount) }
                                                .padding(vertical = AppSpacing.sm),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "+S/ ${amount.toInt()}",
                                                color = GoldPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Información y Juego Responsable
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CardShape)
                                .border(1.dp, ObsidianBorder, CardShape),
                            colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                            shape = CardShape
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.md),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(AppSpacing.sm))
                                Column {
                                    Text(
                                        text = "Juego Responsable y Seguro",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "BetDayLite v1.0.0 • Clean Architecture + Compose",
                                        fontSize = 11.sp,
                                        color = TextTertiary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    title: String,
    value: String,
    valueColor: Color
) {
    Column {
        Text(
            text = title,
            fontSize = 11.sp,
            color = TextTertiary
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor
        )
    }
}
