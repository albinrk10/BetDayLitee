package com.albin.betdaylite.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.albin.betdaylite.core.designsystem.AppSpacing
import com.albin.betdaylite.core.designsystem.NegativeRed
import com.albin.betdaylite.core.designsystem.NeonGreen
import com.albin.betdaylite.core.designsystem.PendingAmber
import com.albin.betdaylite.core.designsystem.PillShape
import com.albin.betdaylite.domain.entities.BetStatus

@Composable
fun StatusBadge(
    status: BetStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon) = when (status) {
        BetStatus.PENDING -> Triple(
            PendingAmber.copy(alpha = 0.18f),
            PendingAmber,
            Icons.Default.Schedule
        )
        BetStatus.WON -> Triple(
            NeonGreen.copy(alpha = 0.18f),
            NeonGreen,
            Icons.Default.CheckCircle
        )
        BetStatus.LOST -> Triple(
            NegativeRed.copy(alpha = 0.18f),
            NegativeRed,
            Icons.Default.Close
        )
    }

    Row(
        modifier = modifier
            .clip(PillShape)
            .background(bgColor)
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status.label,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(AppSpacing.xxs))
        Text(
            text = status.label.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
