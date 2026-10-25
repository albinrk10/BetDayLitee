package com.albin.betdaylite.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albin.betdaylite.core.constants.BetConstants
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.CardShape
import com.albin.betdaylite.core.designsystem.GoldPrimary
import com.albin.betdaylite.core.designsystem.NegativeRed
import com.albin.betdaylite.core.designsystem.NeonGreen
import com.albin.betdaylite.core.designsystem.ObsidianBackground
import com.albin.betdaylite.core.designsystem.ObsidianBorder
import com.albin.betdaylite.core.designsystem.ObsidianSurface
import com.albin.betdaylite.core.designsystem.ObsidianSurfaceVariant
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.core.designsystem.SheetTopShape
import com.albin.betdaylite.core.designsystem.TextPrimary
import com.albin.betdaylite.core.designsystem.TextSecondary
import com.albin.betdaylite.core.designsystem.TextTertiary
import com.albin.betdaylite.core.designsystem.TextDisabled
import com.albin.betdaylite.domain.entities.BetPick
import com.albin.betdaylite.domain.entities.BetSlip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetSlipBottomSheet(
    betSlip: BetSlip?,
    walletBalance: Double,
    onDismiss: () -> Unit,
    onConfirmBet: (stake: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    if (betSlip == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var stakeText by remember(betSlip) { mutableStateOf(betSlip.stake.toInt().toString()) }
    val currentStake = stakeText.toDoubleOrNull() ?: 0.0
    val potentialPayout = currentStake * betSlip.odd
    val isInsufficientBalance = currentStake > walletBalance
    val isValidStake = currentStake > 0 && !isInsufficientBalance

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianSurface,
        shape = SheetTopShape,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = AppSpacing.sm)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(PillShape)
                    .background(ObsidianBorder)
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.lg)
                .padding(bottom = AppSpacing.xl)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TICKET DE APUESTA",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    letterSpacing = 1.sp
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            // Tarjeta de la Selección
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CardShape)
                    .background(ObsidianSurfaceVariant)
                    .border(1.dp, ObsidianBorder, CardShape)
                    .padding(AppSpacing.md)
            ) {
                Column {
                    Text(
                        text = "${betSlip.match.homeTeam.name} vs ${betSlip.match.awayTeam.name}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = betSlip.match.league.name,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.sm))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Predicción: ",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(NeonGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = AppSpacing.sm, vertical = 2.dp)
                            ) {
                                Text(
                                    text = betSlip.pick.label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonGreen
                                )
                            }
                        }

                        Text(
                            text = "@ ${BetConstants.formatOdd(betSlip.odd)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Montos rápidos
            Text(
                text = "SELECCIONA O INGRESA TU MONTO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextTertiary,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                BetConstants.QUICK_STAKES.forEach { stake ->
                    val isChipSelected = currentStake == stake
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(PillShape)
                            .background(if (isChipSelected) GoldPrimary else ObsidianSurfaceVariant)
                            .border(
                                1.dp,
                                if (isChipSelected) GoldPrimary else ObsidianBorder,
                                PillShape
                            )
                            .clickable { stakeText = stake.toInt().toString() }
                            .padding(vertical = AppSpacing.xs),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "S/ ${stake.toInt()}",
                            color = if (isChipSelected) Color(0xFF1B1300) else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            // Campo de texto de monto
            OutlinedTextField(
                value = stakeText,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 6) {
                        stakeText = newValue
                    }
                },
                label = { Text("Monto de apuesta (S/)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedLabelColor = GoldPrimary,
                    unfocusedLabelColor = TextSecondary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = ObsidianBackground,
                    unfocusedContainerColor = ObsidianBackground
                ),
                shape = CardShape
            )

            // Alerta si el saldo es insuficiente
            AnimatedVisibility(visible = isInsufficientBalance) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Alerta",
                        tint = NegativeRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " Saldo insuficiente (Disponible: ${BetConstants.formatCurrency(walletBalance)})",
                        color = NegativeRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.md))
            Divider(color = ObsidianBorder)
            Spacer(modifier = Modifier.height(AppSpacing.md))

            // Resumen de Ganancia Potencial
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ganancia Potencial",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Text(
                    text = BetConstants.formatCurrency(potentialPayout),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NeonGreen
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.lg))

            // Botón Confirmar
            Button(
                onClick = {
                    if (isValidStake) {
                        onConfirmBet(currentStake)
                    }
                },
                enabled = isValidStake,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color(0xFF1B1300),
                    disabledContainerColor = ObsidianBorder,
                    disabledContentColor = TextDisabled
                ),
                shape = PillShape
            ) {
                Text(
                    text = "Confirmar Apuesta • ${BetConstants.formatCurrency(currentStake)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
